package service.Network

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.TilePlacedMessage
import entity.*
import service.RootService

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

    /**
     *  The function [disconnect] is to disconnect the server
     *  if we leave an online Game
     */
    fun disconnect() {
        client?.apply {
            if (sessionID != null) leaveGame("Goodbye!")
            if (isOpen) disconnect()
        }
        client = null
        updateConnectionState(ConnectionState.DISCONNECTED)
    }

    /**
     *  The function[hostGame] is to start a Game as a Host
     *
     *  @param secret The secret to make a secure connection
     *  @param name Name of the host
     *  @param  sessionID Write a sessionID if you want else  you get one from the server
     */
    fun hostGame(secret: String = "game23d", name: String, sessionID: String?) {
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

    /**
     *  The function[hostGame] join a Game as a client
     *
     *  @param secret The secret to make a secure connection
     *  @param name Name of the host
     *  @param  sessionID The sessionID of the Game you want to join
     */
    fun joinGame(secret: String = "game23d", name: String, sessionID: String) {
        if (!connect(secret, name)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        client?.joinGame(sessionID, "Hello!")

        updateConnectionState(ConnectionState.GUEST_WAITING_FOR_CONFIRMATION)
    }

    /**
     * The function [startNewHostedGame] start a new HostGame
     *
     * @param hostPlayer The host
     * @param guestPlayerNames all other joined players
     */
    fun startNewHostedGame(hostPlayer: String, guestPlayerNames: MutableList<String>) {
        check(connectionState == ConnectionState.WAITING_FOR_GUEST)
        { "currently not prepared to start a new hosted game." }
        guestPlayerNames.add(0, hostPlayer)
        TODO("Refresh to the Gui that the GUI gets all player")
    }


    /**
     * The function [startNewJoinedGame] start a new game by joining it.
     *
     * @param message The message you are getting from the host
     * to initialize the game from your game
     */
    fun startNewJoinedGame(message: GameInitMessage, playerName: String) {
        check(connectionState == ConnectionState.WAITING_FOR_INIT)
        { "not waiting for game init message. " }
        val routeTiles = rootService.networkMappingService.toRouteTiles(message.tileList)
        val players = rootService.networkMappingService.toEntityPlayer(message.players)
        if (players[0].name == playerName) {
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        } else {
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
        }
    }

    /**
     *  The class [sendGameMessage] the class send an initMessage
     *  from the currentGame after initialized a new game.
     *
     */
    fun sendGameInitMessage() {
        val networkPlayers = rootService.networkMappingService.toNetworkPlayer()
        val gameMode = rootService.networkMappingService.toGameMode()
        val tileList = rootService.networkMappingService.toTileTypeList()
        val message = GameInitMessage(
            networkPlayers,
            gameMode,
            tileList
        )
        for(otherPlayer in client?.otherPlayers!!){
          if(networkPlayers[0].name == otherPlayer) {
              updateConnectionState(ConnectionState.WAITING_FOR_OPPONENTS_TURN)
          }
      }
        updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        client?.sendGameActionMessage(message)
    }


    /**
     * The function [sendPlacedTile] make the placedTile to a message to send
     * to other online Player
     *
     * @param placedTile PlacedTile is the tile you choose to placed
     * @param coordinate Coordinate is where the tile placed
     */
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
     * which have the information for the tile coordinate und rotation
     */
    fun receivedTilePLacedMessage(message: TilePlacedMessage) {
        check(
            connectionState in listOf(
                ConnectionState.PLAYING_MY_TURN, ConnectionState.WAITING_FOR_OPPONENTS_TURN
            )
        ) { "currently not expecting an opponent's turn." }
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val rotation = message.rotation
        for (i in 0 until rotation) {
            TODO()
            //rootService.playerTurnService.rotateTileRight()
        }
        val space = Coordinate(message.qCoordinate, message.rCoordinate)
        TODO()
        //rootService.playerTurnService.placeRouteTile(currentGame.routeTiles[0], space)
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