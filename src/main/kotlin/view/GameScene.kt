package view

import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * Represents the game scene containing a hexagonal grid.
 *
 */

class GameScene(indigoApp: IndigoApplication) :
    BoardGameScene(1920, 1080, background = ImageVisual("PlainBackground_FCE6BD.png")), Refreshable {
    // Hexagonal grid for the game board
    private val hexagonGrid: HexagonGrid<HexagonView> =
        HexagonGrid(coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL, posX = 820, posY = 420)

    // reserveStack component
    private val reserveStack = HexagonView(posX = 869, posY = 870, visual = ImageVisual("plaintile.png"))

    // undoButton component
    private val undoButton =
        view.components.Button(posX = 650, posY = 880, width = 160, height = 68, text = "Undo", fontSize = 40)

    // redoButton component
    private val redoButton =
        view.components.Button(posX = 650, posY = 980, width = 160, height = 68, text = "Redo", fontSize = 40)

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
    private var player1handTile = view.components.Label(posX = 127, posY = 184, text = "")
    private var player2handTile = view.components.Label(posX = 1619, posY = 184, text = "")
    private var player3handTile = view.components.Label(posX = 127, posY = 754, text = "")
    private var player4handTile = view.components.Label(posX = 1619, posY = 754, text = "")

    // PlayerturnHighlight components currentPlayer bekommt einen blauen Hintergrund (egal welcher Player)
    private val player1turnHighlight = HexagonView(posX = 102, posY = 149, visual = ColorVisual(Color.BLUE))
    private val player2turnHighlight = HexagonView(posX = 1595, posY = 149, visual = ColorVisual(Color.BLUE))
    private val player3turnHighlight = HexagonView(posX = 102, posY = 719, visual = ColorVisual(Color.BLUE))
    private val player4turnHighlight = HexagonView(posX = 1595, posY = 719, visual = ColorVisual(Color.BLUE))

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

    /**
     * Initializes the GameScene with default values and sets up the hexagonal grid.
     */
    init {
        initializeGameBoardGrid()

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
            gate6Token2
        )

    }

    /**
     * Initialize game board grid
     */

    /**
     * Initialize game board grid
     */
    fun initializeGameBoardGrid() {
        // Populate the hexagonal grid with HexagonView instances
        for (row in -4..4) {
            for (col in -4..4) {
                val hexagon = HexagonView(visual = ImageVisual("plaintile.png"))
                hexagon.resize(width = 110, height = 110)
                hexagon.scaleY(0.6)
                hexagon.scaleX(0.6)
                hexagonGrid[col, row] = hexagon
            }
        }
        // Rotate the hexagonal grid by -30 degrees
        hexagonGrid.rotate(-30)
        // Middle Tile
        hexagonGrid[0, 0]?.visual = ImageVisual("middletile.png")
        // Gate Tiles
        //1 (oben und dann im Uhrzeigersinn die GateTiles)
        hexagonGrid[4, -4]?.visual = ImageVisual("gatetile1.png")
        //2
        hexagonGrid[4, 0]?.visual = ImageVisual("gatetile2.png")
        //3
        hexagonGrid[0, 4]?.visual = ImageVisual("gatetile3.png")
        //4
        hexagonGrid[-4, 4]?.visual = ImageVisual("gatetile4.png")
        //5
        hexagonGrid[-4, 0]?.visual = ImageVisual("gatetile5.png")
        //6
        hexagonGrid[0, -4]?.visual = ImageVisual("gatetile6.png")

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
}
