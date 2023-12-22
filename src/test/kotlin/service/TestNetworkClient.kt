package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.TilePlacedMessage
import service.network.IndigoNetworkClient
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.GameActionResponse
import tools.aqua.bgw.net.common.response.JoinGameResponse

/**
 *  The test class [TestNetworkClient] creates a test client for the [NetworkConnectionTests]
 */
class TestNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    networkService: TestNetworkService
) :
    IndigoNetworkClient(playerName, host, secret, networkService) {
    var onGameActionResponse: ((GameActionResponse) -> Unit)? = null
    var onCreateGameResponse: ((CreateGameResponse) -> Unit)? = null
    var onInitMessage: ((GameInitMessage, String) -> Unit)? = null
    var onPlayerJoined: ((PlayerJoinedNotification) -> Unit)? = null
    var onPlayerLeft : ((PlayerLeftNotification)-> Unit)?=null
    var onJoinedGameResponse: ((JoinGameResponse) -> Unit)? = null
    var onTilePlacedReceived: ((TilePlacedMessage, String) -> Unit)? = null

    private val testNetworkService: TestNetworkService?
        get() = networkService as? TestNetworkService

    override fun onCreateGameResponse(response: CreateGameResponse) {
        onCreateGameResponse?.invoke(response)
        super.onCreateGameResponse(response)
    }

    override fun onJoinGameResponse(response: JoinGameResponse) {
        onJoinedGameResponse?.invoke(response)
        super.onJoinGameResponse(response)
    }

    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        onPlayerJoined?.invoke(notification)
        super.onPlayerJoined(notification)
    }

    override fun onPlayerLeft(notification: PlayerLeftNotification) {
        onPlayerLeft?.invoke(notification)
        super.onPlayerLeft(notification)
    }
    override fun onGameActionResponse(response: GameActionResponse) {
        onGameActionResponse?.invoke(response)
        super.onGameActionResponse(response)
    }

    @GameActionReceiver
    override fun onTilePlacedReceived(message: TilePlacedMessage, sender: String) {
        onTilePlacedReceived?.invoke(message, sender)
        super.onTilePlacedReceived(message, sender)
    }

    /**
     *
     */
    @GameActionReceiver
    override fun onInitReceivedMessage(message: GameInitMessage, sender: String) {
        onInitMessage?.invoke(message, sender)
        super.onInitReceivedMessage(message, sender)
    }

}