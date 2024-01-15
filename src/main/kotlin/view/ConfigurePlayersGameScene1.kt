package view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import view.components.Button
import view.components.Label
import view.components.PlayerView

/**
 * Represents the new player menu scene.
 *
 * This scene shows a list of all actual players, their token and turn.
 * It is possible to add players if less than four players are actually in the player list
 * and start the game which leads to the game scene (?)
 *
 * The layout and design of these components are defined in this class.
 *
 */
class ConfigurePlayersGameScene1(indigoApp: IndigoApplication) : BoardGameScene(1920, 1080), Refreshable {
    // UI components for configuring players



    private val titleLabel = Label(397, 80, 1058, 155, "Configure Players", 128)
    private val startGameButton = Button(1047, 755, 528, 207, "Start game", 48).apply {
        isDisabled = true
        onMouseClicked = {
            if(indigoApp.aiGame) indigoApp.showMenuScene(indigoApp.aiMenuScene)
            else indigoApp.showGameScene(indigoApp.gameScene) }
    }
    private val addNewPlayerButton = Button(340, 755, 528, 207, "Add new player", 48).
    apply {
        onMouseClicked = {indigoApp.showMenuScene(indigoApp.newPlayerScene)}}

    val players = GridPane<ComponentView>(posX = 397, posY = 300, layoutFromCenter = false, rows = 0, columns = 2, spacing = 50)

    /**
     * Initializes the NewPlayerMenuScene with default values and sets up UI components.
     */
    init {
        // Set the background image for the scene
        background = ImageVisual("ThreeGemsBackground.png")
        // Set the initial opacity of the scene
        opacity = 1.0
        // Add components to the scene
        addComponents(titleLabel)
        addComponents(startGameButton)
        addComponents(addNewPlayerButton)
        // Create a grid for displaying player information

        //players[0,0] = Label(text = "Player 1 :", width = 200, height = 65, fontSize = 40)
        //players[0,1] = Label(text = "Player 2 :", width = 200, height = 65, fontSize = 40)
        //players[0,2] = Label(text = "Player 3 :", width = 200, height = 65, fontSize = 40)
        //players[0,3] = Label(text = "Player 4 :", width = 200, height = 65, fontSize = 40)

        addComponents(players)

    }

    fun addPlayer(indigoApp : IndigoApplication,name : String, turn :String, color:String)
    {
        //val counter = indigoApp.players.size-1
        //players[1,counter] = PlayerView(name,color,turn)
        //startGameButton.isDisabled = Indigo.players.size<2
        //addNewPlayerButton.isDisabled= indigoApp.players.size==4
        val currentRows = players.rows
        players.addRows(currentRows)
        players[0,currentRows] = Label(text = "Player ${players.rows} :", width = 200, height = 65, fontSize = 40)
        players[1,currentRows] = PlayerView(name,color,turn)
        startGameButton.isDisabled = players.rows<2
        addNewPlayerButton.isDisabled= players.rows==4
    }
    //add refreshScene addnewplayerButton is disabled = indigoApp.players.size>=4
}