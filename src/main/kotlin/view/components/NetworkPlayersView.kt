package view.components

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.visual.ColorVisual

class NetworkPlayersView (posX : Int = 0, posY : Int = 0) :
    Pane<ComponentView>(posX,posY,1643,141, visual = ColorVisual(253,240,216))
{
    private val button = Button(1212,16,367,109,"Configure",30)
    private val label = Label(141,44,956,58,"Player1 : Name", 48)

    init {
        this.add(button)
        this.add(label)
    }
}
