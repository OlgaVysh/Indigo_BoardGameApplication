package service

import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.TilePlacedMessage
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.response.*
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification

/**
 *  The class [IndigoNetworkClient] describe the Interaction with bgw net as a client
 *
 *  @property playerName The player Name is the name of the player
 *  @property host The name of the host
 *  @property  secret The secret for the project to make a secure connection
 *  @property networkService The networkService of the game
 *  to use the functions of the class
 */
class IndigoNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    var networkService: NetworkService,
) : BoardGameClient(playerName, host, secret, NetworkLogging.VERBOSE) {
    /**
     * @property sessionID The SessionID is to connect to the specific game
     */
    var sessionID: String? = null

    var otherPlayers = mutableListOf<String>()

    override fun onCreateGameResponse(response: CreateGameResponse) {
        BoardGameApplication.runOnGUIThread {
            check(networkService.connectionState == ConnectionState.HOST_WAITING_FOR_CONFIRMATION)
            { "unexpected CreateGameResponse" }

            when (response.status) {
                CreateGameResponseStatus.SUCCESS -> {
                    networkService.updateConnectionState(ConnectionState.WAITING_FOR_GUEST)
                    sessionID = response.sessionID
                }
                else -> {}
            }
        }
    }

    override fun onJoinGameResponse(response: JoinGameResponse) {
        BoardGameApplication.runOnGUIThread {
            check(networkService.connectionState == ConnectionState.GUEST_WAITING_FOR_CONFIRMATION)
            { "unexpected JoinGameResponse" }

            when (response.status) {
                JoinGameResponseStatus.SUCCESS -> {
                    otherPlayers = response.opponents.toMutableList()
                    sessionID = response.sessionID
                    networkService.updateConnectionState(ConnectionState.WAITING_FOR_INIT)
                }

                else -> {}
            }
        }
    }

    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        BoardGameApplication.runOnGUIThread {
            check(networkService.connectionState == ConnectionState.WAITING_FOR_GUEST)
            { "not awaiting any guests." }

            otherPlayers.add(notification.sender)
            networkService.startNewHostedGame(playerName,otherPlayers)
        }
    }

    @Suppress("UNUSED_PARAMETER", "unused")
    @GameActionReceiver
    fun onTilePlacedReceived(message: TilePlacedMessage, sender: String) {
        BoardGameApplication.runOnGUIThread {
            networkService.receivedTilePLacedMessage(message)
        }
    }

    override fun onGameActionResponse(response: GameActionResponse) {
        BoardGameApplication.runOnGUIThread {
            check(
                networkService.connectionState == ConnectionState.PLAYING_MY_TURN ||
                        networkService.connectionState == ConnectionState.WAITING_FOR_OPPONENTS_TURN
            )
            { "not currently playing in a network game." }
        }
    }

    @Suppress("UNUSED_PARAMETER", "unused")
    @GameActionReceiver
    fun onInitReceivedMessage(message: GameInitMessage, sender: String) {
        BoardGameApplication.runOnGUIThread {
            networkService.startNewJoinedGame(
                message = message,
                playerName
            )
        }
    }
}