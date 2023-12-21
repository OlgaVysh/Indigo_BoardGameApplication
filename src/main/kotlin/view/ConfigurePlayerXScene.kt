package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label
/**
 * Represents the game menu scene in which a player can choose his Tokencolor and his turn.
 *
 * This scene is displayed when a player is chosen to be configured. It includes
 * a label indicating the title,
 * two comboboxes with matching labels for tokencolor and turn to choose from
 * and a button for saving the configurations.
 *
 * The layout and design of these components are defined in this class.
 *
 * @property rootService An instance of RootService to access game-related functionalities.
 */
class ConfigurePlayerXScene(private val rootService: RootService) : MenuScene(990, 1080) {
    private val game = rootService.currentGame
    //irgendwie noch an zu bearbeitenden Spieler drankommen jetzt noch X
    //ich hab die Box noch nicht durch tokens geändert ist etwas komplizierter
    private val titleLabel = Label(42, 80, 900, 116, "Configure Player X", 96)
    private val saveChangeButton = Button(247,779,528,207, "Save changes", 48)
    private val colorLabel = Label(80, 370, width = 300, text = "color : ", fontSize = 48)
    private val colorBox = ComboBox<String>(320, 370, 454.34, 69, prompt = "Select your color!")
    private val turnLabel = Label(80, 535, width = 300, text = "turn : ", fontSize = 48)
    private val turnBox = ComboBox<Int>(320, 535, 454.34, 69, prompt = "Select your turn!")




    init {
        opacity = 0.5
        addComponents(titleLabel)
        addComponents(saveChangeButton)
        addComponents(turnBox)
        addComponents(turnLabel)
        addComponents(colorLabel)
        addComponents(colorBox)
        turnBox.items = mutableListOf(1, 2, 3, 4)
        colorBox.items = mutableListOf("blue", "purple", "red", "white")
    }
}