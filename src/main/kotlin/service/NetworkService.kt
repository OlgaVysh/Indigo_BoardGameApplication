package service

/**
 *  The class [NetworkService] is to have all function  with the network for online gaming.
 *
 *  @property rootService the rootService to have the information of the currentgame
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
     *  The private fun [connect] is to make a connection to the server
     *
     *  @param secret The secret to make a secure connection  to the Server
     *  @param name The name of the player
     */
    fun connect(secret: String = "game23d", name: String): Boolean {
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
     * The function [updateConnectionState] updated the Statemachine
     *
     * @param newState The state which the function update
     */
    fun updateConnectionState(newState: ConnectionState) {
        this.connectionState = newState
    }

}