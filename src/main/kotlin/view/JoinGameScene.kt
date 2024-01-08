package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label

/**
 * Represents the join game menu scene.
 *
 * This scene when a player enters the option to join a game in the scene before ("Szenenname").
 *
 * The layout and design of these components are defined in this class.
 * @param rootService The root service providing access to game-related functionality.
 */
class JoinGameScene(private val rootService: RootService) : MenuScene(990, 1080) {
    //private val game = rootService.currentGame
    //iregendwie noch an zu bearbeitenden Spieler drankommen jetzt noch X
    private val titleLabel = Label(42, 80, 900, 116, "Configure Player X", 96)
    private val joinButton = Button(247, 800, 528, 207, "Save changes", 48)
    private val nameLabel = Label(80, 370, width = 300, text = "Name : ", fontSize = 48)
    private val nameInput: TextField = TextField(width = 420, height = 69, posX = 320, posY = 370)

    private val idLabel = Label(80, 535, width = 300, text = "Game id : ", fontSize = 48)
    private val idInput: TextField = TextField(width = 420, height = 69, posX = 320, posY = 535)

    private val aiLabel = Label(80, 700, width = 300, text = "AI : ", fontSize = 48)

    private val yesLabel = Label(370, 700, width = 80, text = "yes", fontSize = 48)
    private val noLabel = Label(670, 700, width = 80, text = "no", fontSize = 48)

    private val toggleGroup = ToggleGroup()
    private val yesButton = RadioButton(posX = 320, posY = 700, toggleGroup = toggleGroup)
    private val noButton = RadioButton(posX = 620, posY = 700, toggleGroup = toggleGroup)

    /**
     * Initializes the JoinGameScene with default values and sets up UI components.
     */
    init {
        // Set the initial opacity of the scene
        opacity = 0.5
        // Add components to the scene
        addComponents(titleLabel)
        addComponents(joinButton)
        addComponents(idLabel)
        addComponents(nameLabel)
        addComponents(aiLabel)
        addComponents(nameInput)
        addComponents(idInput)
        addComponents(yesButton)
        addComponents(noButton)
        addComponents(yesLabel)
        addComponents(noLabel)
        // Set alignment for specific labels
        nameLabel.alignment = Alignment.CENTER_LEFT
        aiLabel.alignment = Alignment.CENTER_LEFT
        idLabel.alignment = Alignment.CENTER_LEFT
    }
}