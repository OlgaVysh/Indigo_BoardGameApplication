package view

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the join game menu scene.
 *
 * This scene when a player enters the option to join a game in the scene before ("Szenenname").
 *
 * The layout and design of these components are defined in this class.
 */
class JoinGameScene(val indigoApp: IndigoApplication) : MenuScene(990, 1080), Refreshable {
    //private val game = rootService.currentGame

    private val rootService = indigoApp.rootService

    //iregendwie noch an zu bearbeitenden Spieler drankommen jetzt noch X
    private val titleLabel = Label(42, 80, 900, 116, "Configure Player X", 96)

    private val nameLabel = Label(80, 370, width = 300, text = "Name : ", fontSize = 48)
    private val nameInput: TextField = TextField(width = 420, height = 69, posX = 320, posY = 370).apply {
        onKeyTyped = {
            joinButton.isDisabled = this.text.isBlank() || idInput.text.isBlank()
        }
    }

    private val idLabel = Label(80, 535, width = 300, text = "Session ID : ", fontSize = 40)
    private val idInput: TextField = TextField(width = 420, height = 69, posX = 320, posY = 535).apply {
        onKeyTyped = {
            joinButton.isDisabled = this.text.isBlank() || nameInput.text.isBlank()
        }
    }

    private val joinButton = Button(247, 800, 528, 207, "Join", 48).apply {
        isDisabled = nameInput.text.isBlank() || idInput.text.isBlank()
        onMouseClicked = {
            var isAi = false
            if (yesButton.isSelected) isAi = true
            if (nameInput.text.isNotBlank() && idInput.text.isNotBlank()) indigoApp.rootService.networkService.joinGame(
                name = nameInput.name, sessionID = idLabel.name, isAi = isAi
            )
        }
    }

    private val aiLabel = Label(80, 700, width = 300, text = "AI : ", fontSize = 48)

    private val yesLabel = Label(370, 700, width = 80, text = "yes", fontSize = 48)
    private val noLabel = Label(670, 700, width = 80, text = "no", fontSize = 48)

    private val toggleGroup = ToggleGroup()
    private val yesButton = RadioButton(posX = 320, posY = 700, toggleGroup = toggleGroup)
    private val noButton = RadioButton(posX = 620, posY = 700, toggleGroup = toggleGroup)

    private val textMessageLabel = Label(
        15, 340, 960, 480, "Waiting for Confirmation", 48
    ).apply {
        visual = ImageVisual("button.png")
        isVisible = false
        isDisabled = true
    }

    /**
     * Initializes the JoinGameScene with default values and sets up UI components.
     */
    init {
        // Set the initial opacity of the scene
        opacity = 0.5
        // Add components to the scene
        addComponents(titleLabel)
        addComponents(idLabel)
        addComponents(nameLabel)
        addComponents(aiLabel)
        addComponents(nameInput)
        addComponents(idInput)
        addComponents(yesButton)
        addComponents(noButton)
        addComponents(yesLabel)
        addComponents(noLabel)
        addComponents(joinButton)
        addComponents(textMessageLabel)

        // Set alignment for specific labels
        nameLabel.alignment = Alignment.CENTER_LEFT
        aiLabel.alignment = Alignment.CENTER_LEFT
        idLabel.alignment = Alignment.CENTER_LEFT
    }

    override fun refreshAfterJoinGame() {
        textMessageLabel.isVisible = true
        textMessageLabel.isDisabled = false
        textMessageLabel.text = rootService.networkService.connectionState.name
    }

    override fun refreshAfterOnJoinGameResponse(responseStatus: JoinGameResponseStatus) {
        textMessageLabel.isVisible = true
        textMessageLabel.isDisabled = false
        when (responseStatus) {
            JoinGameResponseStatus.SUCCESS -> textMessageLabel.text = "Waiting for Host to finish Game Configuration"
            JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> {
                textMessageLabel.text = "Already connected to the Game"
            }

            JoinGameResponseStatus.INVALID_SESSION_ID -> {
                textMessageLabel.text = JoinGameResponseStatus.INVALID_SESSION_ID.name + "\n" + "try another Session ID"
            }

            JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> {
                textMessageLabel.text =
                    JoinGameResponseStatus.INVALID_SESSION_ID.name + "\n" + "try another Player Name"
            }

            else -> {
                textMessageLabel.text = "Another failure"
            }
        }
        playAnimation(DelayAnimation(duration = 2000).apply {
            onFinished = {
                if (responseStatus != JoinGameResponseStatus.SUCCESS) {
                    textMessageLabel.isVisible = false
                    textMessageLabel.isDisabled = true
                }
            }
        })
    }

    override fun refreshAfterStartNewJoinedGame() {
        indigoApp.showGameScene(indigoApp.gameScene)
        indigoApp.hideMenuScene()
        //TODO(//indigoApp.gameScene.refreshAfterStartNewGame)
    }
}