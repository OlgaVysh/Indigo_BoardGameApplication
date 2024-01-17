package view

import entity.*
import entity.Coordinate
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.style.BorderColor
import tools.aqua.bgw.style.BorderWidth
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * Represents the game scene containing a hexagonal grid.
 */

class GameScene(val indigoApp: IndigoApplication) :
    BoardGameScene(1920, 1080, background = ImageVisual("PlainBackground_FCE6BD.png")), Refreshable {

    private val rootService = indigoApp.rootService

    //view von dem angeklickten Place am GameBoard (für Highlighten)
    private var chosenPlace : HexagonView? = null

    //coordinaten vom angeklicktem Place am GameBoard(zum Platzieren)
    private var chosenCol : Int? = null
    private var chosenRow : Int? = null

    //View von dem Tile vom currentPlayer (zum Platzieren)
    private var tileToPlace : HexagonView? = null

    // Hexagonal grid for the game board
    private val hexagonGrid: HexagonGrid<HexagonView> =
        HexagonGrid(coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL, posX = 820, posY = 420)

    // reserveStack component
    private val reserveStack = HexagonView(posX = 869, posY = 870, visual = ImageVisual("plaintile.png"))

    // undoButton component
    private val undoButton =
        view.components.Button(posX = 650, posY = 880, width = 160, height = 68, text = "Undo", fontSize = 40).apply {
            if (indigoApp.networkMode) {isVisible = false
            isDisabled = true}
            onMouseClicked= {indigoApp.rootService.playerTurnService.undo()}
        }

    // redoButton component
    private val redoButton =
        view.components.Button(posX = 650, posY = 980, width = 160, height = 68, text = "Redo", fontSize = 40).apply {
            if (indigoApp.networkMode){ isVisible = false
            isDisabled = true}
            onMouseClicked= {indigoApp.rootService.playerTurnService.redo()}
        }

    // saveButton component
    private val saveButton =
        view.components.Button(posX = 1055, posY = 980, width = 160, height = 68, text = "Save", fontSize = 40)
            .apply { onMouseClicked = { indigoApp.showMenuScene(indigoApp.saveGameScene) } }

    // Player components
    private var player1Label =
        view.components.Label(posX = 110, posY = 68, width = 200, text = "Player 1", fontSize = 48)
    private var player2Label =
        view.components.Label(posX = 1573, posY = 68, width = 200, text = "Player 2", fontSize = 48)
    private var player3Label =
        view.components.Label(posX = 143, posY = 917, width = 200, text = "Player 3", fontSize = 48)
    private var player4Label =
        view.components.Label(posX = 1579, posY = 917, width = 200, text = "Player 4", fontSize = 48)

    // PlayerScore components
    private var player1ScoreLabel =
        view.components.Label(posX = 110, posY = 114, width = 200, text = "0 points", fontSize = 48)
    private var player2ScoreLabel =
        view.components.Label(posX = 1573, posY = 114, width = 200, text = "0 points", fontSize = 48)
    private var player3ScoreLabel =
        view.components.Label(posX = 143, posY = 964, width = 200, text = "0 points", fontSize = 48)
    private var player4ScoreLabel =
        view.components.Label(posX = 1579, posY = 964, width = 200, text = "0 points", fontSize = 48)

    // PlayerToken components
    private var player1Token = view.components.Label(posX = 60, posY = 70, text = "")
    private var player2Token = view.components.Label(posX = 1523, posY = 70, text = "")
    private var player3Token = view.components.Label(posX = 93, posY = 921, text = "")
    private var player4Token = view.components.Label(posX = 1529, posY = 921, text = "")

    // PlayergreenGem components
    private var player1greenGem = view.components.Label(posX = 399, posY = 142, text = "", width = 81, height = 65)
    private var player2greenGem = view.components.Label(posX = 1351, posY = 142, text = "", width = 81, height = 65)
    private var player3greenGem = view.components.Label(posX = 399, posY = 830, text = "", width = 81, height = 65)
    private var player4greenGem = view.components.Label(posX = 1351, posY = 830, text = "", width = 81, height = 65)

    // PlayeryellowGem components
    private var player1yellowGem = view.components.Label(posX = 490, posY = 86, text = "", width = 81, height = 65)
    private var player2yellowGem = view.components.Label(posX = 1262, posY = 86, text = "", width = 81, height = 65)
    private var player3yellowGem = view.components.Label(posX = 490, posY = 764, text = "", width = 81, height = 65)
    private var player4yellowGem = view.components.Label(posX = 1262, posY = 764, text = "", width = 81, height = 65)

    // PlayergreenGemCounter components
    private var player1greenGemCounter = view.components.Label(posX = 413, posY = 95, text = "0", fontSize = 48)
    private var player2greenGemCounter = view.components.Label(posX = 1365, posY = 95, text = "0", fontSize = 48)
    private var player3greenGemCounter = view.components.Label(posX = 413, posY = 783, text = "0", fontSize = 48)
    private var player4greenGemCounter = view.components.Label(posX = 1365, posY = 783, text = "0", fontSize = 48)

    // PlayeryellowGemCounter components
    private var player1yellowGemCounter = view.components.Label(posX = 504, posY = 39, text = "0", fontSize = 48)
    private var player2yellowGemCounter = view.components.Label(posX = 1276, posY = 39, text = "0", fontSize = 48)
    private var player3yellowGemCounter = view.components.Label(posX = 504, posY = 717, text = "0", fontSize = 48)
    private var player4yellowGemCounter = view.components.Label(posX = 1276, posY = 717, text = "0", fontSize = 48)

    // PlayerHandTile components
    // player1 oben links player2 oben rechts player3 unten links player4 unten rechts
    private var player1handTile = view.components.Label(posX = 127, posY = 184, text = "")
    private var player2handTile = view.components.Label(posX = 1619, posY = 184, text = "")
    private var player3handTile = view.components.Label(posX = 127, posY = 754, text = "")
    private var player4handTile = view.components.Label(posX = 1619, posY = 754, text = "")

    // PlayerturnHighlight components (currentPlayer bekommt einen blauen Hintergrund (egal welcher Player))
    private val player1turnHighlight = HexagonView(posX = 102, posY = 149, visual = ColorVisual(Color.BLUE))
    private val player2turnHighlight = HexagonView(posX = 1595, posY = 149, visual = ColorVisual(Color.BLUE))
    private val player3turnHighlight = HexagonView(posX = 102, posY = 719, visual = ColorVisual(Color.BLUE))
    private val player4turnHighlight = HexagonView(posX = 1595, posY = 719, visual = ColorVisual(Color.BLUE))

    // PlayerRotateButton components
    private val player1leftButton =
        Button(posX = 60, posY = 360, width = 80, height = 50, visual = ImageVisual("leftbutton.png"))
    private val player1rightButton =
        Button(posX = 240, posY = 360, width = 80, height = 50, visual = ImageVisual("rightbutton.png"))
    private val player1checkButton =
        Button(posX = 150, posY = 360, width = 80, height = 50, visual = ImageVisual("checkbutton.png"))

    private val player2leftButton =
        Button(posX = 1553, posY = 360, width = 80, height = 50, visual = ImageVisual("leftbutton.png"))
    private val player2rightButton =
        Button(posX = 1733, posY = 360, width = 80, height = 50, visual = ImageVisual("rightbutton.png"))
    private val player2checkButton =
        Button(posX = 1643, posY = 360, width = 80, height = 50, visual = ImageVisual("checkbutton.png"))

    private val player3leftButton =
        Button(posX = 60, posY = 680, width = 80, height = 50, visual = ImageVisual("leftbutton.png"))
    private val player3rightButton =
        Button(posX = 240, posY = 680, width = 80, height = 50, visual = ImageVisual("rightbutton.png"))
    private val player3checkButton =
        Button(posX = 150, posY = 680, width = 80, height = 50, visual = ImageVisual("checkbutton.png"))

    private val player4leftButton =
        Button(posX = 1553, posY = 680, width = 80, height = 50, visual = ImageVisual("leftbutton.png"))
    private val player4rightButton =
        Button(posX = 1733, posY = 680, width = 80, height = 50, visual = ImageVisual("rightbutton.png"))
    private val player4checkButton =
        Button(posX = 1643, posY = 680, width = 80, height = 50, visual = ImageVisual("checkbutton.png"))


    // GateToken components
    //gate oben links und dann im Uhrzeigersinn
    private var gate1Token1 = view.components.Label(posX = 666, posY = 105, text = "")
    private var gate1Token2 = view.components.Label(posX = 748, posY = 57, text = "")

    private var gate2Token1 = view.components.Label(posX = 1065, posY = 57, text = "")
    private var gate2Token2 = view.components.Label(posX = 1147, posY = 105, text = "")

    private var gate3Token1 = view.components.Label(posX = 1306, posY = 385, text = "")
    private var gate3Token2 = view.components.Label(posX = 1306, posY = 475, text = "")

    private var gate4Token1 = view.components.Label(posX = 1147, posY = 750, text = "")
    private var gate4Token2 = view.components.Label(posX = 1065, posY = 797, text = "")

    private var gate5Token1 = view.components.Label(posX = 748, posY = 797, text = "")
    private var gate5Token2 = view.components.Label(posX = 666, posY = 750, text = "")

    private var gate6Token1 = view.components.Label(posX = 510, posY = 475, text = "")
    private var gate6Token2 = view.components.Label(posX = 510, posY = 385, text = "")

    // GameboardGem components
    private var blueGem = view.components.Label(posX = 911, posY = 432, text = "", width = 40, height = 32)

    //oben links im Uhrzeigersinn
    private var greenGem1 = view.components.Label(posX = 890, posY = 405, text = "", width = 40, height = 32)
    private var greenGem2 = view.components.Label(posX = 930, posY = 402, text = "", width = 40, height = 32)
    private var greenGem3 = view.components.Label(posX = 944, posY = 439, text = "", width = 40, height = 32)
    private var greenGem4 = view.components.Label(posX = 912, posY = 465, text = "", width = 40, height = 32)
    private var greenGem5 = view.components.Label(posX = 875, posY = 438, text = "", width = 40, height = 32)

    //oben im Uhrzeigersinn
    private var yellowGem1 = view.components.Label(posX = 910, posY = 70, text = "", width = 40, height = 32)
    private var yellowGem2 = view.components.Label(posX = 1226, posY = 248, text = "", width = 40, height = 32)
    private var yellowGem3 = view.components.Label(posX = 1226, posY = 612, text = "", width = 40, height = 32)
    private var yellowGem4 = view.components.Label(posX = 910, posY = 797, text = "", width = 40, height = 32)
    private var yellowGem5 = view.components.Label(posX = 590, posY = 613, text = "", width = 40, height = 32)
    private var yellowGem6 = view.components.Label(posX = 590, posY = 246, text = "", width = 40, height = 32)

    /**
     * Initializes the GameScene with default values and sets up the hexagonal grid.
     */
    init {
        initializeGameBoardGrid()
        hexagonGrid.rotate(60)
        hexagonGrid.reposition(900, 340)

        //initialize reserveStack
        reserveStack.rotate(30)
        reserveStack.resize(width = 110, height = 110)
        reserveStack.scaleY(0.6)
        reserveStack.scaleX(0.6)

        //initialize tokenViews
        player1Token.visual = ImageVisual("tokenwhite.png")
        player2Token.visual = ImageVisual("tokenblue.png")
        player3Token.visual = ImageVisual("tokenpurple.png")
        player4Token.visual = ImageVisual("tokenred.png")

        //initialize gemViews
        player1greenGem.visual = ImageVisual("greenGem.png")
        player2greenGem.visual = ImageVisual("greenGem.png")
        player3greenGem.visual = ImageVisual("greenGem.png")
        player4greenGem.visual = ImageVisual("greenGem.png")
        player1yellowGem.visual = ImageVisual("yellowGem.png")
        player2yellowGem.visual = ImageVisual("yellowGem.png")
        player3yellowGem.visual = ImageVisual("yellowGem.png")
        player4yellowGem.visual = ImageVisual("yellowGem.png")

        blueGem.visual = ImageVisual("blueGem.png")
        greenGem1.visual = ImageVisual("greenGem.png")
        greenGem2.visual = ImageVisual("greenGem.png")
        greenGem3.visual = ImageVisual("greenGem.png")
        greenGem4.visual = ImageVisual("greenGem.png")
        greenGem5.visual = ImageVisual("greenGem.png")
        yellowGem1.visual = ImageVisual("yellowGem.png")
        yellowGem2.visual = ImageVisual("yellowGem.png")
        yellowGem3.visual = ImageVisual("yellowGem.png")
        yellowGem4.visual = ImageVisual("yellowGem.png")
        yellowGem5.visual = ImageVisual("yellowGem.png")
        yellowGem6.visual = ImageVisual("yellowGem.png")


        //initialize handTileViews
        player1handTile.rotate(30)
        player2handTile.rotate(30)
        player3handTile.rotate(30)
        player4handTile.rotate(30)
        player1handTile.resize(130.5, 151.5)
        player2handTile.resize(130.5, 151.5)
        player3handTile.resize(130.5, 151.5)
        player4handTile.resize(130.5, 151.5)
        player1handTile.visual = ImageVisual("tile1.png")
        player2handTile.visual = ImageVisual("tile2.png")
        player3handTile.visual = ImageVisual("tile3.png")
        player4handTile.visual = ImageVisual("tile4.png")

        //initialize playerturnHighlightViews
        player1turnHighlight.rotate(30)
        player2turnHighlight.rotate(30)
        player3turnHighlight.rotate(30)
        player4turnHighlight.rotate(30)
        player1turnHighlight.resize(147.9, 171.7)
        player2turnHighlight.resize(147.9, 171.7)
        player3turnHighlight.resize(147.9, 171.7)
        player4turnHighlight.resize(147.9, 171.7)

        //initialize gateTokenViews
        gate1Token1.visual = ImageVisual("tokenred.png")
        gate1Token2.visual = ImageVisual("tokenblue.png")

        gate2Token1.visual = ImageVisual("tokenred.png")
        gate2Token2.visual = ImageVisual("tokenblue.png")

        gate3Token1.visual = ImageVisual("tokenred.png")
        gate3Token2.visual = ImageVisual("tokenblue.png")

        gate4Token1.visual = ImageVisual("tokenred.png")
        gate4Token2.visual = ImageVisual("tokenblue.png")

        gate5Token1.visual = ImageVisual("tokenred.png")
        gate5Token2.visual = ImageVisual("tokenblue.png")

        gate6Token1.visual = ImageVisual("tokenred.png")
        gate6Token2.visual = ImageVisual("tokenblue.png")


        // Add the hexagonal grid to the components of the game scene
        addComponents(
            hexagonGrid,
            reserveStack,
            undoButton,
            redoButton,
            saveButton,
            player1Label,
            player2Label,
            player3Label,
            player4Label,
            player1ScoreLabel,
            player2ScoreLabel,
            player3ScoreLabel,
            player4ScoreLabel,
            player1Token,
            player2Token,
            player3Token,
            player4Token,
            player1greenGem,
            player2greenGem,
            player3greenGem,
            player4greenGem,
            player1yellowGem,
            player2yellowGem,
            player3yellowGem,
            player4yellowGem,
            player1greenGemCounter,
            player2greenGemCounter,
            player3greenGemCounter,
            player4greenGemCounter,
            player1yellowGemCounter,
            player2yellowGemCounter,
            player3yellowGemCounter,
            player4yellowGemCounter,
            player1turnHighlight,
            player2turnHighlight,
            player3turnHighlight,
            player4turnHighlight,
            player1handTile,
            player2handTile,
            player3handTile,
            player4handTile,
            gate1Token1,
            gate1Token2,
            gate2Token1,
            gate2Token2,
            gate3Token1,
            gate3Token2,
            gate4Token1,
            gate4Token2,
            gate5Token1,
            gate5Token2,
            gate6Token1,
            gate6Token2,
            blueGem,
            greenGem1,
            greenGem2,
            greenGem3,
            greenGem4,
            greenGem5,
            yellowGem1,
            yellowGem2,
            yellowGem3,
            yellowGem4,
            yellowGem5,
            yellowGem6,
            player1leftButton,
            player1rightButton,
            player1checkButton,
            player2leftButton,
            player2rightButton,
            player2checkButton,
            player3leftButton,
            player3rightButton,
            player3checkButton,
            player4leftButton,
            player4rightButton,
            player4checkButton
        )

    }

    /**
     * invokes player actions to the buttons
     */
    private fun invokeButtons(players : List<Player>)
    {
        val count = players.size

        val currentGame = indigoApp.rootService.currentGame
        checkNotNull(currentGame)
        val currentPlayerIndex = currentGame.currentPlayerIndex

        val tile = players[currentPlayerIndex].handTile!!

        val leftButtons =
           listOf(player1leftButton,player2leftButton,player3leftButton,player4leftButton)

        val rightButtons =
            listOf(player1rightButton,player2rightButton,player3rightButton,player4rightButton)

        val checkButtons =
            listOf(player1checkButton,player2checkButton,player3checkButton,player4checkButton)

        for(i in 0 until count)
        {
            leftButtons[i].onMouseClicked = {rootService.playerTurnService.rotateTileLeft(tile)}
        }

        for(i in 0 until count)
        {
            rightButtons[i].onMouseClicked = {rootService.playerTurnService.rotateTileRight(tile)}
        }

        for(i in 0 until count)
        {
            checkButtons[i].onMouseClicked = {callPlaceTile(tile)}
        }
    }

    /**
     * Initialize game board grid
     */
    private fun initializeGameBoardGrid() {
        // Populate the hexagonal grid with HexagonView instances
        for (row in -4..4) {
            for (col in -4..4) {
                val hexagon = HexagonView(visual = ImageVisual("plaintile.png")).apply{
                onMouseClicked = {
                    chooseTile(this, col, row)}}

                hexagon.resize(width = 110, height = 110)
                hexagon.scaleY(0.6)
                hexagon.scaleX(0.6)
                hexagonGrid[col, row] = hexagon
            }
        }
        // Rotate the hexagonal grid by -30 degrees
        hexagonGrid.rotate(-30)
        // Middle Tile
        hexagonGrid[0, 0]?.apply { visual = ImageVisual("middletile.png")
        isDisabled = true}
        // Gate Tiles
        //1
        hexagonGrid[0, -4]?.apply{visual = ImageVisual("gatetile1.png")
            isDisabled = true}
        //2
        hexagonGrid[4, -4]?.apply{visual = ImageVisual("gatetile2.png")
            isDisabled = true}
        //3
        hexagonGrid[4, 0]?.apply{visual = ImageVisual("gatetile3.png")
            isDisabled = true}
        //4
        hexagonGrid[0, 4]?.apply{visual = ImageVisual("gatetile4.png")
            isDisabled = true}
        //5
        hexagonGrid[-4, 4]?.apply{visual = ImageVisual("gatetile5.png")
            isDisabled = true}
        //6
        hexagonGrid[-4, 0]?.apply{visual = ImageVisual("gatetile6.png")
            isDisabled = true}


        //hides the unused hexagon components left and right
        for (i in 0..3) {
            for (j in -4..-4 + i) {
                //left board side
                hexagonGrid[-(i + 1), j]?.visual = Visual.EMPTY
                //right board side
                hexagonGrid[(i + 1), -j]?.visual = Visual.EMPTY
            }
        }
    }

    /**
     *  Initialize GateTokens
     */
    private fun initialzeGateTokens() {
        val guiGateTokens = listOf(
            gate2Token1,
            gate2Token2,
            gate3Token1,
            gate3Token2,
            gate4Token1,
            gate4Token2,
            gate5Token1,
            gate5Token2,
            gate6Token1,
            gate6Token2,
            gate1Token1,
            gate1Token2,
        )
        val currentGame = indigoApp.rootService.currentGame
        val entityGateTokens = currentGame!!.gameBoard.gateTokens

        for (i in entityGateTokens.indices) {
            guiGateTokens[i].visual = entityGateTokens[i].color.toImg()
        }
    }

    /**
     * Initializes the GUI after a new game was created. Creates views of players, their tiles and gates
     */
    override fun refreshAfterStartGame()
    {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        val players = game.players
        setPlayers(players)
        initialzeGateTokens()
        invokeButtons(players)
    }

    /**
     * For every player set a view of name and token color and disable superfluous players views
     */
    private fun setPlayers(players : List<Player>)
    {
        val count = players.size

        for (n in 0 until count)
        {

            when (n) {
                0 -> {
                    player1Label.text = players[0].name
                    getGem(player1Token, players[0].color)
                }

                1 -> {
                    player2Label.text = players[1].name
                    getGem(player2Token, players[1].color)
                }

                2 -> {
                    player3Label.text = players[2].name
                    getGem(player3Token, players[2].color)
                }

                3 -> {
                    player4Label.text = players[3].name
                    getGem(player4Token, players[3].color)
                }
            }
        }


        for (a in count until 4)
        {
            when(a)
            {
                2 -> {
                    player3Label.apply {isVisible = false}
                    player3ScoreLabel.apply { isDisabled = true
                        isVisible = false}
                    player3Token.apply { isDisabled = true
                        isVisible = false}
                    player3greenGem.apply { isDisabled = true
                        isVisible = false}
                    player3greenGemCounter.apply { isDisabled = true
                        isVisible = false}
                    player3yellowGem.apply { isDisabled = true
                        isVisible = false}
                    player3yellowGemCounter.apply { isDisabled = true
                        isVisible = false}
                    player3turnHighlight.apply { isDisabled = true
                        isVisible = false}
                    player3handTile.apply { isDisabled = true
                        isVisible = false}
                    player3leftButton.apply { isDisabled = true
                        isVisible = false}
                    player3rightButton.apply { isDisabled = true
                        isVisible = false}
                    player3checkButton.apply { isDisabled = true
                        isVisible = false}
                }

                3 -> {
                    player4Label.apply { isDisabled = true
                        isVisible = false}
                    player4ScoreLabel.apply { isDisabled = true
                        isVisible = false}
                    player4Token.apply { isDisabled = true
                        isVisible = false}
                    player4greenGem.apply { isDisabled = true
                        isVisible = false}
                    player4greenGemCounter.apply { isDisabled = true
                        isVisible = false}
                    player4yellowGem.apply { isDisabled = true
                        isVisible = false}
                    player4yellowGemCounter.apply { isDisabled = true
                        isVisible = false}
                    player4turnHighlight.apply { isDisabled = true
                        isVisible = false}
                    player4handTile.apply { isDisabled = true
                        isVisible = false}
                    player4leftButton.apply { isDisabled = true
                        isVisible = false}
                    player4rightButton.apply { isDisabled = true
                        isVisible = false}
                    player4checkButton.apply { isDisabled = true
                        isVisible = false}
                }
            }
        }

    }


    /**
     * Sets Token-Label with an Image of the given TokenColor
     */
    private fun getGem(label : view.components.Label, color: TokenColor)
    {
        when(color) {
            TokenColor.WHITE -> label.apply{visual = ImageVisual("tokenwhite.png") }

            TokenColor.PURPLE -> label.apply{visual = ImageVisual("tokenpurple.png") }

            TokenColor.BLUE -> label.apply{visual = ImageVisual("tokenblue.png") }

            TokenColor.RED -> label.apply{visual = ImageVisual("tokenred.png") }
        }
    }

    /**
     * Update the Gui highlight after refresh called
     */
    override fun refreshAfterChangePlayer() {
        val playerHighlights =
            listOf(player1turnHighlight, player2turnHighlight, player3turnHighlight, player4turnHighlight)
        val playerRotateRights = listOf(player1rightButton, player2rightButton, player3rightButton, player4rightButton)
        val playerRotateLefts = listOf(player1leftButton, player2leftButton, player3leftButton, player4leftButton)
        val playerRotateCheck = listOf(player1checkButton, player2checkButton, player3checkButton, player4checkButton)
        val currentGame = indigoApp.rootService.currentGame
        checkNotNull(currentGame)
        val currentPlayerIndex = currentGame.currentPlayerIndex
        for (i in playerHighlights.indices) {
            if (i == currentPlayerIndex) {
                playerHighlights[i].isVisible = true
                playerRotateRights[i].isVisible = true
                playerRotateRights[i].isDisabled = false
                playerRotateLefts[i].isVisible = true
                playerRotateLefts[i].isDisabled = false
                playerRotateCheck[i].isVisible = true
                playerRotateCheck[i].isDisabled = false
            } else {
                playerHighlights[i].isVisible = false
                playerRotateRights[i].isVisible = false
                playerRotateRights[i].isDisabled = true
                playerRotateLefts[i].isVisible = false
                playerRotateLefts[i].isDisabled = true
                playerRotateCheck[i].isVisible = false
                playerRotateCheck[i].isDisabled = true

            }
        }
        refreshAfterMove()
    }

    /**
     *  Update the GameScene with the new tile from the currentplayer
     */
    override fun refreshAfterDistributeNewTile() {
        val playerTile = listOf(player1handTile, player2handTile, player3handTile, player4handTile)
        val currentGame = indigoApp.rootService.currentGame
        checkNotNull(currentGame)
        val players = currentGame.players
        val currentIndex = currentGame.currentPlayerIndex
        val currentHandTile = players[currentIndex].handTile
        if (currentHandTile == null) {
            playerTile[currentIndex].isVisible = false
        } else {
            playerTile[currentIndex].visual = currentHandTile.type.toImg()
            playerTile[currentIndex].isVisible = true
        }
    }
    /**
     *  refreshes the GameScene after EndGame was called
     *
     *  @throws IllegalStateException if no game is running
     */
    override fun refreshAfterEndGame() {
        val game = indigoApp.rootService.currentGame
        checkNotNull(game) { "No game found."}
        indigoApp.showMenuScene(indigoApp.endGameMenuScene)
    }
    /**
     *  refreshes the GameScene after PlayerTurn rotateTileLeft was called
     *
     *  @throws IllegalStateException if no game is running
     */
    override fun refreshAfterLeftRotation(currentPlayerIndex: Int) {
        val game = indigoApp.rootService.currentGame
        checkNotNull(game) { "No game found."}
        val handTileLabels =
                listOf(player1handTile, player2handTile, player3handTile, player4handTile)
        handTileLabels[currentPlayerIndex].rotate(-60)
    }
    /**
     *  refreshes the GameScene after PlayerTurn rotateTileRight was called
     *
     *  @throws IllegalStateException if no game is running
     */
    override fun refreshAfterRightRotation(currentPlayerIndex: Int) {
        val game = indigoApp.rootService.currentGame
        checkNotNull(game) { "No game found."}
        val handTileLabels =
                listOf(player1handTile, player2handTile, player3handTile, player4handTile)
        handTileLabels[currentPlayerIndex].rotate(60)
    }

    /*override fun refreshAfterRedo() {
        val coordinate = chosenCol?.let { chosenRow?.let { it1 -> Coordinate(it, it1) } }
        if (coordinate != null) {
            refreshAfterPlaceTile(coordinate)
        }
    }
    */

    /*override fun refreshAfterUndo() {

    }
    */

    override fun refreshAfterPlaceTile(coordinate: Coordinate, tile: Tile) {
        val col = coordinate.column
        val row = coordinate.row
        checkNotNull(tileToPlace){"keine Ahnung wie"}
        hexagonGrid[col,row] = tileToPlace!! //wie kann es null sein
        tileToPlace=null
    }

    /**
     * Sets chosen space back after the player tried to place tile at the occupied space
     */
    override fun refreshAfterCheckPlacement() {
        refreshAfterMove()
    }

    /**
     * Highlights the clicked tile and sets [chosenPlace] to the currently chosen space
     * Saves the coordinates of the chosen space in [chosenCol] and [chosenRow]
     */
    private fun chooseTile(tile : HexagonView, col : Int, row: Int)
    {
        if(chosenPlace!=null) chosenPlace!!.visual.borderWidth= BorderWidth(0)

        tile.apply{
            visual.borderWidth = BorderWidth(5)
            visual.borderColor = BorderColor.RED}

        chosenPlace = tile
        chosenRow = row
        chosenCol = col

    }

    /**
     * Creates Coordinate Object of chosenRow and chosenCol. Asserts if no space was chosen yet.
     * tileToPlace saves the latest View of currentPlayers tile to place on the gameBoard
     * Calls placeRouteTile with the given tile and created Coordinate
     */
    private fun callPlaceTile(tile: Tile)
    {
        checkNotNull(chosenPlace){"Please, choose space on the board and press ✓"}

            val coordinates = Coordinate(chosenRow!!,chosenCol!!)
            val currentPlayer = rootService.currentGame!!.currentPlayerIndex
            when(currentPlayer)
            {
                0 -> { tileToPlace = HexagonView(visual = player1handTile.visual)
                    .apply { resize(width = 110, height = 110)
                        scaleY(0.6)
                        scaleX(0.6)
                    }}
                1 -> { tileToPlace = HexagonView(visual =player2handTile.visual)
                    .apply { resize(width = 110, height = 110)
                        scaleY(0.6)
                        scaleX(0.6)}}
                2 -> { tileToPlace = HexagonView(visual =player3handTile.visual)
                    .apply { resize(width = 110, height = 110)
                        scaleY(0.6)
                        scaleX(0.6)}}
                3 -> { tileToPlace = HexagonView(visual =player4handTile.visual)
                    .apply { resize(width = 110, height = 110)
                        scaleY(0.6)
                        scaleX(0.6)}}
            }
            rootService.playerTurnService.placeRouteTile(coordinates,tile)

    }

    /**
     * Sets chosen space back after the player tried to or placed his/her tile
     */
    private fun refreshAfterMove()
    {
        if(chosenPlace!=null)
        {
            chosenPlace!!.visual.borderWidth= BorderWidth(0)
            chosenPlace=null
            chosenRow=null
            chosenCol=null

        }

    }


}
