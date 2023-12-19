package view
import view.components.*

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

class ConfigureNetworkPlayersScene ( games : List<String>): BoardGameScene(1920, 1080,  background =  ImageVisual("PlainBackground_FCE6BD.png"))
{
    private val label = Label(453,21,1050,155,"Configure Players", 120)
    private val grid = GridPane<NetworkPlayersView>(960,484,1,games.size,10, true)
    private val size =  games.size-1
    private val addButton = Button(188,806,528,207,"Add new player",40)
    private val startButton = Button(1217,806,528,207,"Start new game",40)

    init {

        for (i in 0 ..size)
        {
            grid[0,i] = NetworkPlayersView()
        }
        addComponents(label,grid,addButton,startButton)
    }
}
