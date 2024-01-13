package view


import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import view.components.Button
import view.components.Label

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
class ConfigurePlayersGameScene(indigoApp: IndigoApplication) : BoardGameScene(1920, 1080), Refreshable {
    // UI components for configuring players

    private val titleLabel = Label(397, 80, 1058, 155, "Configure Players", 128)
    private val startGameButton = Button(1047, 755, 528, 207, "Start game", 48).apply {
        isDisabled = indigoApp.players.size<=2
        onMouseClicked = {
            if(indigoApp.aiGame) indigoApp.showMenuScene(indigoApp.aiMenuScene)
            else indigoApp.showGameScene(indigoApp.gameScene) }
    }
    private val addNewPlayerButton = Button(340, 755, 528, 207, "Add new player", 48).
    apply { isDisabled = indigoApp.players.size>=4
    onMouseClicked = {indigoApp.showMenuScene(indigoApp.newPlayerScene)}}

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
        val playerLabel =
            GridPane<Label>(posX = 400, posY = 300, layoutFromCenter = false, rows = 4, columns = 3, spacing = 105)
        // Labels and visuals for player information
        val gemLabel1 = Label(text = "")
        val gemLabel2 = Label(text = "")
        val gemLabel3 = Label(text = "")
        val gemLabel4 = Label(text = "")
        //val gemLabelList = mutableListOf<Label>(gemLabel1, gemLabel2, gemLabel3, gemLabel4)
        //Einbauen sobald Service fertig
        //ggf. label.width individuell (max player names)
        /*
        for (i in 0 .. game!!.players.size){
           playerLabel[0,i] = Label(text = "Player $i: game.players[i].name", width = 600, fontSize = 48)
           gemLabelList[i].visual = game.players[i].color.toImg()
           playerLabel[1,i] = gemLabelList[i]
        }

         */

        // Test values (replace with actual player data once the service is ready)
        playerLabel[0, 0] = Label(text = "Player 1: " + "Olga", width = 600, fontSize = 48)
        playerLabel[0, 0]!!.alignment = Alignment.TOP_LEFT
        gemLabel1.visual = ImageVisual("tokenblue.png")
        playerLabel[1, 0] = gemLabel1
        playerLabel[2, 0] = Label(text = "turn: " + "1st", width = 450, fontSize = 48)
        playerLabel[2, 0]!!.alignment = Alignment.TOP_LEFT

        playerLabel[0, 1] = Label(text = "Player 2: " + "Semi", width = 600, fontSize = 48)
        playerLabel[0, 1]!!.alignment = Alignment.TOP_LEFT
        gemLabel2.visual = ImageVisual("tokenred.png")
        playerLabel[1, 1] = gemLabel2
        playerLabel[2, 1] = Label(text = "turn: " + "2nd", width = 450, fontSize = 48)
        playerLabel[2, 1]!!.alignment = Alignment.TOP_LEFT

        //längster noch lesbarer Name ist 15 Chars lang
        playerLabel[0, 2] = Label(text = "Player 3: " + "012345678901234", width = 600, fontSize = 48)
        playerLabel[0, 2]!!.alignment = Alignment.TOP_LEFT
        gemLabel3.visual = ImageVisual("tokenwhite.png")
        playerLabel[1, 2] = gemLabel3
        playerLabel[2, 2] = Label(text = "turn: " + "3rd", width = 450, fontSize = 48)
        playerLabel[2, 2]!!.alignment = Alignment.TOP_LEFT

        playerLabel[0, 3] = Label(text = "Player 4: " + "Maria", width = 600, fontSize = 48)
        playerLabel[0, 3]!!.alignment = Alignment.TOP_LEFT
        gemLabel4.visual = ImageVisual("tokenpurple.png")
        playerLabel[1, 3] = gemLabel4
        playerLabel[2, 3] = Label(text = "turn: " + "4th", width = 450, fontSize = 48)
        playerLabel[2, 3]!!.alignment = Alignment.TOP_LEFT

        // Add the player information grid to the components of the scene
        addComponents(playerLabel)

    }

}