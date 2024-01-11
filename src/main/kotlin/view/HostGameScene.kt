package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label

/**
 * Represents the scene for hosting a game.
 *
 * This scene includes elements for entering host and game details, such as name and game ID, along with a button to initiate hosting a game.
 * The layout and interaction logic for these components are defined within this class.
 *
 * @property rootService An instance of RootService to access and manipulate game data.
 */
class HostGameScene (private val rootService: RootService) : MenuScene(990, 1080) {
    private val game = rootService.currentGame

    // Label to display the "Host Game" Header.
    private val hostLabel = Label (42, 105, 900,116,"Host Game",120)

    // Label for the Name.
    private val nameLabel = Label(80, 321,300,58,"Name :",48)

    // Label for the ID.
    private val sessionIdLabel = Label (80, 486,350,116,"Session ID :",48)

    // TextField for the host's name with an event handler to
    // enable or disable the host game button based on text input.
    private val hostName: TextField = TextField(
        width = 454,
        height = 69,
        posX = 390,
        posY = 320,
    ).apply {
        onKeyTyped = {
            hostGameButton.isDisabled = this.text.isBlank()
        }
    }

    // TextField for entering the game ID.
    private val gameId: TextField = TextField(
        width = 454,
        height = 69,
        posX = 390,
        posY = 510
    )

    // Button for host to game.
    private val hostGameButton = Button(247, 698,532,207,"Host game",48)

    // Setting the scene's opacity and adding all components
    init {
        opacity = 0.5
        addComponents(
            hostLabel,
            nameLabel,
            sessionIdLabel,
            hostName,
            gameId,
            hostGameButton
        )
    }
}