package view

import entity.CPUPlayer
import entity.Player
import entity.TokenColor
import service.network.ConnectionState
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.RadioButton
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.ToggleGroup
import tools.aqua.bgw.core.MenuScene
import view.components.Button
import view.components.Label
import view.components.NetworkPlayersView

class NewPlayerScene(val indigoApp: IndigoApplication) : MenuScene(990, 1080), Refreshable {

    private val colors = mutableListOf("blue", "purple", "red", "white")
    private val turns = mutableListOf(1, 2, 3, 4)
    private var aiPlayer = false

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
    private val yesButton = RadioButton(posX = 350, posY = 710, toggleGroup = toggleGroup).apply {
        onMouseClicked = {
            aiPlayer = true
            indigoApp.aiGame = true
        }
    }
    private val noButton = RadioButton(posX = 550, posY = 710, isSelected = true, toggleGroup = toggleGroup)

    private val playerName: TextField = TextField(
        width = 350,
        height = 50,
        posX = 360,
        posY = 320,
    ).apply {
        onKeyTyped = {
            addNewPlayerButton.isDisabled = this.text.isBlank()
        }
    }

    private val addNewPlayerButton = Button(250, 780, 528, 207, "Add new player", 48).apply {
        isDisabled = playerName.text.isBlank()
        onMouseClicked = {
            indigoApp.hideMenuScene()
            val newPlayerColor =
                when (colorBox.selectedItem) {
                    "purple" -> TokenColor.PURPLE
                    "blue" -> TokenColor.BLUE
                    "red" -> TokenColor.RED
                    "white " -> TokenColor.WHITE
                    else -> indigoApp.avaibleColors[0]
                }
            indigoApp.avaibleColors.remove(newPlayerColor)
            var newPlayer = Player(name, color = newPlayerColor)
            if (aiPlayer) {
                newPlayer = CPUPlayer(name = playerName.text, color = newPlayerColor)
            } else
                newPlayer = Player(name = playerName.text, color = newPlayerColor)
            when (turnBox.selectedItem) {
                1 -> indigoApp.players[0] = newPlayer
                2 -> indigoApp.players[1] = newPlayer
                3 -> indigoApp.players[2] = newPlayer
                4 -> indigoApp.players[3] = newPlayer
                else -> {
                    indigoApp.players.add(newPlayer)
                }
            }
            addPlayerToTheScene(newPlayer)
            refreshScene()
            //refreshAfterAddNewPlayer()
        }
    }

    private val playerAge: TextField = TextField(
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
            nameLabel,
            ageLabel,
            turnBox,
            turnLabel,
            colorLabel,
            colorBox,
            yesLabel,
            noLabel,
            aiLabel,
            yesButton,
            noButton,
            addNewPlayerButton,
            playerName,
            playerAge
        )

        turnBox.items = turns
        colorBox.items = colors

    }

    private fun addPlayerToTheScene(newPlayer: Player) {
        val connectionState = indigoApp.rootService.networkService.connectionState
        if (connectionState != ConnectionState.DISCONNECTED) {
            val configureNetworkPlayersScene = indigoApp.networkConfigureScene
            val currentRows = configureNetworkPlayersScene.grid.rows
            if (currentRows < 4) {
                configureNetworkPlayersScene.grid.addRows(currentRows)
                val grid = configureNetworkPlayersScene.grid
                configureNetworkPlayersScene.grid[0, currentRows] = NetworkPlayersView(0, 0).apply {
                    label.text = "Player " + grid.rows + ": " + newPlayer.name
                    this.button.onMouseClicked = {
                        indigoApp.showMenuScene(indigoApp.configurePlayerXScene)
                    }
                }
                indigoApp.networkConfigureScene.addButton.isDisabled = grid.rows == 4
                indigoApp.networkConfigureScene.startButton.isDisabled = grid.rows < 2
            }
        } else {
            indigoApp.configurePlayersScene.addPlayerView(
                playerName.text,
                newPlayer.color,
                turnBox.selectedItem,
                aiPlayer
            )
        }
    }

    /**
     * Clears all the components of the scene and deletes the chosen color and turn from the
     * ComboBoxes, so every color and turn can only be chosen once
     */
    private fun refreshScene() {
        noButton.isSelected = true
        playerName.text = ""
        playerAge.text = ""

        colors.remove(colorBox.selectedItem)
        colorBox.items = colors
        colorBox.selectedItem = null

        turns.remove(turnBox.selectedItem)
        turnBox.items = turns
        turnBox.selectedItem = null
        addNewPlayerButton.isDisabled = true
        aiPlayer = false
    }
}
