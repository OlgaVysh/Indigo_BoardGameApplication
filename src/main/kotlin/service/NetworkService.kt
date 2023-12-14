package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.TilePlacedMessage
import entity.Coordinate
import entity.Edge
import entity.Player
import entity.Tile

/**
 *  The class [NetworkService] is to have all function  with the network for online gaming.
 *
 *  @property rootService the rootService to have the information of the current game
 */
class NetworkService(private val rootService: RootService) {
    companion object {
        /** URL of the BGW Net to play at SoPra23d */
        const val SERVER_ADDRESS = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

        /** Name of the game as registered with the server */
        const val GAME_ID = "Indigo"
    }

    /**
     *  The connection begins with disconnected
     */
    var connectionState: ConnectionState = ConnectionState.DISCONNECTED
        private set

    /**
     *  The client if we are the client
     */
    var client: IndigoNetworkClient? = null

    fun disconnect() {
        client?.apply {
            if (sessionID != null) leaveGame("Goodbye!")
            if (isOpen) disconnect()
        }
        client = null
        updateConnectionState(ConnectionState.DISCONNECTED)
    }

    fun hostGame(secret: String, name: String, sessionID: String?) {
        if (!connect(secret, name)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        if (sessionID.isNullOrBlank()) {
            client?.createGame(GAME_ID, "Welcome!")
        } else {
            client?.createGame(GAME_ID, sessionID, "Welcome!")
        }
        updateConnectionState(ConnectionState.HOST_WAITING_FOR_CONFIRMATION)
    }


    fun joinGame(secret: String, name: String, sessionID: String) {
        if (!connect(secret, name)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        client?.joinGame(sessionID, "Hello!")

        updateConnectionState(ConnectionState.GUEST_WAITING_FOR_CONFIRMATION)
    }

    fun startNewHostedGame(hostPlayer: Player, guestPlayerNames:MutableList<Player> ) {
        check(connectionState == ConnectionState.WAITING_FOR_GUEST)
        { "currently not prepared to start a new hosted game." }
        val players = guestPlayerNames
            players.add(0,hostPlayer)
        // comment until the Service for startNewGame are implemented
       // rootService.gameService.startNewGame(players)
        val networkPlayers = rootService.networkMappingService.toNetworkPlayer()
        val gameMode = rootService.networkMappingService.toGameModeMapping()
        val tileList = rootService.networkMappingService.toTileTypeList()
        val message = GameInitMessage(
             networkPlayers, gameMode, tileList
        )
        updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        client?.sendGameActionMessage(message)
    }

    fun startNewJoinedGame(){

    }
    fun sendPlacedTile(placedTile: Tile, coordinate: Coordinate) {
        require(connectionState == ConnectionState.PLAYING_MY_TURN) { "not my turn" }
        val rotation = placedTile.edges.indexOf(Edge.ZERO)
        val qCoordinate = coordinate.column
        val rCoordinate = coordinate.row
        val message = TilePlacedMessage(
            rotation, qCoordinate, rCoordinate
        )
        client?.sendGameActionMessage(message)
    }
    /**
     * The function [receivedTilePLacedMessage] make the received message
     * to an Action in the Indigo game
     *
     * @param message The message is from the other player in the network mode
     * which have the information for the tile Coordinate und rotation
     */
    fun  receivedTilePLacedMessage(message: TilePlacedMessage) {
        check(
            connectionState in listOf(
                ConnectionState.PLAYING_MY_TURN, ConnectionState.WAITING_FOR_OPPONENTS_TURN
            )
        ) { "currently not expecting an opponent's turn." }
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val rotation = message.rotation
        for (i in 0..rotation) {
            // TODO:
        }
    }

    /**
     *  The private fun [connect] is to make a connection to the server
     *
     *  @param secret The secret to make a secure connection  to the Server
     *  @param name The name of the player
     */
    private fun connect(secret: String = "game23d", name: String): Boolean {
        require(connectionState == ConnectionState.DISCONNECTED && client == null)
        { "already connected to another game" }
        require(name.isNotBlank()) { "player name must be given" }
        val newClient =
            IndigoNetworkClient(
                playerName = name,
                host = SERVER_ADDRESS,
                secret = secret,
                networkService = this
            )
        return if (newClient.connect()) {
            this.client = newClient
            true
        } else {
            false
        }
    }

    /**
     * The function [updateConnectionState] updated the State machine
     *
     * @param newState The state which the function update
     */
    fun updateConnectionState(newState: ConnectionState) {
        this.connectionState = newState
    }
}