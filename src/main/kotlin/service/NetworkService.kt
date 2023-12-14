package service

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

    var client: IndigoNetworkClient? = null

    /**
     *  The private fun
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
     * Updating the Statemachine
     */
    fun updateConnectionState(newState: ConnectionState) {
        this.connectionState = newState
    }

}