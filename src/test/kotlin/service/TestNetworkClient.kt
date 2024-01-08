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
 * Custom implementation of a network client for testing purposes, extending IndigoNetworkClient.
 *
 * @param playerName The name of the player associated with the network client.
 * @param host The host address for the network connection.
 * @param secret The secret token for secure communication.
 * @param networkService The network service responsible for handling network interactions.
 */
class TestNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    networkService: TestNetworkService
) :
    IndigoNetworkClient(playerName, host, secret, networkService) {
    // Callbacks for various network events
    var onGameActionResponse: ((GameActionResponse) -> Unit)? = null
    var onCreateGameResponse: ((CreateGameResponse) -> Unit)? = null
    var onInitMessage: ((GameInitMessage, String) -> Unit)? = null
    var onPlayerJoined: ((PlayerJoinedNotification) -> Unit)? = null
    var onPlayerLeft: ((PlayerLeftNotification) -> Unit)? = null
    var onJoinedGameResponse: ((JoinGameResponse) -> Unit)? = null
    var onTilePlacedReceived: ((TilePlacedMessage, String) -> Unit)? = null

    // Accessor for the TestNetworkService
    private val testNetworkService: TestNetworkService?
        get() = networkService as? TestNetworkService

    /**
     * Overrides the callback for handling CreateGameResponse events.
     */
    override fun onCreateGameResponse(response: CreateGameResponse) {
        onCreateGameResponse?.invoke(response)
        super.onCreateGameResponse(response)
    }

    /**
     * Overrides the callback for handling JoinGameResponse events.
     */
    override fun onJoinGameResponse(response: JoinGameResponse) {
        onJoinedGameResponse?.invoke(response)
        super.onJoinGameResponse(response)
    }

    /**
     * Overrides the callback for handling PlayerJoinedNotification events.
     */

    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        onPlayerJoined?.invoke(notification)
        super.onPlayerJoined(notification)
    }

    /**
     * Overrides the callback for handling PlayerLeftNotification events.
     */

    override fun onPlayerLeft(notification: PlayerLeftNotification) {
        onPlayerLeft?.invoke(notification)
        super.onPlayerLeft(notification)
    }

    /**
     * Overrides the callback for handling GameActionResponse events.
     */
    override fun onGameActionResponse(response: GameActionResponse) {
        onGameActionResponse?.invoke(response)
        super.onGameActionResponse(response)
    }

    /**
     * Overrides the callback for handling TilePlacedMessage events.
     */
    @GameActionReceiver
    override fun onTilePlacedReceived(message: TilePlacedMessage, sender: String) {
        onTilePlacedReceived?.invoke(message, sender)
        super.onTilePlacedReceived(message, sender)
    }

    /**
     * Overrides the callback for handling GameInitMessage events.
     */

    @GameActionReceiver
    override fun onInitReceivedMessage(message: GameInitMessage, sender: String) {
        onInitMessage?.invoke(message, sender)
        super.onInitReceivedMessage(message, sender)
    }

}