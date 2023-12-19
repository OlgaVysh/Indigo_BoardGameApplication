package view.components
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font

class Label(posX : Int = 0, posY : Int = 0, width : Int = 0, height : Int = 0,
            text : String = "I'm a Label", fontSize : Int = 20) :
    Label(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        text = text,
        font = Font(size = fontSize, family = "Irish Grover")
    )
{
    init {
        this.componentStyle = "-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"
    }
}