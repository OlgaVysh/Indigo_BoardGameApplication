package view

import tools.aqua.bgw.components.layoutviews.CameraPane
import view.components.*
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

class SavedGamesMenuScene ( games : List<String>): BoardGameScene(1920, 1080,  background =  ImageVisual("PlainBackground_FCE6BD.png"))
{
    private val label = Label(566,22,777,155,"Saved games", 120)
    private val grid = GridPane<SavedGameView>(960,540,1,games.size,10,true)
    private val size =  games.size-1
    private val camera = CameraPane(139,176,
        1920,857, target = grid ).apply {
        interactive=true
        isHorizontalLocked = true
        isVerticalLocked = false
        isZoomLocked = true}

    init {

        for (i in 0 ..size)
        {
            grid[0,i] = SavedGameView()
        }
        addComponents(label,camera)
    }

}
