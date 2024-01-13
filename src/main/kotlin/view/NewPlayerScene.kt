package view

import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.RadioButton
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.ToggleGroup
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label

class NewPlayerScene (indigoApp : IndigoApplication) : MenuScene(990, 1080),Refreshable {
    private val label = Label(42, 80, 900, 116, "Configure Player", 96)

    private val nameLabel = Label(65, 293, 300, 98, text = "Name: ", fontSize = 48)
    private val ageLabel = Label(80, 393, 300, 98, text = "Age (opt): ", fontSize = 48)

    private val colorLabel = Label(80, 493, width = 300, text = "Color (opt): ", fontSize = 48)
    private val colorBox = ComboBox<String>(360, 485, 454.34, 69, prompt = "Select your color!")

    private val turnLabel = Label(80, 593, width = 300, text = "Turn (opt): ", fontSize = 48)
    private val turnBox = ComboBox<Int>(360, 585, 454.34, 69, prompt = "Select your turn!")

    private val aiLabel = Label(140, 693, width = 200, text = "AI : ", fontSize = 48)
    private val yesLabel = Label(400, 693, width = 80, text = "yes", fontSize = 48)
    private val noLabel = Label(600, 693, width = 80, text = "no", fontSize = 48)

    private val toggleGroup = ToggleGroup()
    private val yesButton = RadioButton(posX = 350, posY = 710, toggleGroup = toggleGroup)
    private val noButton = RadioButton(posX = 550, posY = 710, toggleGroup = toggleGroup)

    private val addNewPlayerButton = Button(250, 780, 528, 207, "Add new player", 48).
    apply { onMouseClicked = {
        indigoApp.hideMenuScene()
    //refreshAfterAddNewPlayer()
    }
    }

    private val hostName: TextField = TextField(
        width = 350,
        height = 50,
        posX = 360,
        posY = 320,
    ).apply {
        onKeyTyped = {
            addNewPlayerButton.isDisabled = this.text.isBlank()
        }
    }

    private val hostAge: TextField = TextField(
        width = 350,
        height = 50,
        posX = 360,
        posY = 420,
    ).apply {
        onKeyTyped = {
            addNewPlayerButton.isDisabled = this.text.isBlank()
        }
    }

    init {
        opacity = 0.5
        addComponents(
            label,
            nameLabel, ageLabel,
            turnBox, turnLabel,
            colorLabel, colorBox,
            yesLabel, noLabel, aiLabel,
            yesButton, noButton,
            addNewPlayerButton,
            hostName, hostAge
        )

        turnBox.items = mutableListOf(1, 2, 3, 4)
        colorBox.items = mutableListOf("blue", "purple", "red", "white")
    }
}