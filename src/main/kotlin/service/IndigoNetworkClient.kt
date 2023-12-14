package service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging

/**
 *  The class [IndigoNetworkClient] describe the Interaction with bgw net as a client
 *
 *  @property playerName The player Name is the name of the player
 *  @property host The name of the host
 *  @property secret The secret for the project to make a secure connection
 *  @property networkService The networkService of the game
 *  to use the functions of the class
 */
class IndigoNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    var networkService: NetworkService,
    )
    : BoardGameClient(playerName, host, secret, NetworkLogging.VERBOSE) {
    /**
     * @property sessionID The SessionID is to connect to the specific game
     */
    var sessionID: String? = null
}