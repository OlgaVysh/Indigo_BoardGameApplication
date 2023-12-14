package service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging

class IndigoNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    var networkService: NetworkService,
    )
    : BoardGameClient(playerName, host, secret, NetworkLogging.VERBOSE) {
    var sessionID: String? = null
}