package view

import service.RootService
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * Represents the game scene containing a hexagonal grid.
 *
 *
 */

class GameScene( private val indigoApp : IndigoApplication) :
    BoardGameScene(1920, 1080, background = ImageVisual("PlainBackground_FCE6BD.png")) {
    // Hexagonal grid for the game board
    private val hexagonGrid: HexagonGrid<HexagonView> = HexagonGrid(
        coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL,
        posX = 820,
        posY = 420
    )

    /**
     * Initializes the GameScene with default values and sets up the hexagonal grid.
     */
    init {
        // Populate the hexagonal grid with HexagonView instances
        for (row in -4..4) {
            for (col in -4..4) {
                val hexagon = HexagonView(visual = ColorVisual(Color.BLUE))
                hexagon.resize(width = 110, height = 110)
                hexagon.scaleY(0.55)
                hexagon.scaleX(0.55)
                hexagonGrid[col, row] = hexagon
            }
        }
        // Rotate the hexagonal grid by -30 degrees
        hexagonGrid.rotate(-30)
        hexagonGrid[4, -4]?.visual = ImageVisual("gatetile1.png")
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

        // Add the hexagonal grid to the components of the game scene
        addComponents(hexagonGrid)

    }
    //fun initializeGameBoardGrid(){}
}
