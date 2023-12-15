package view
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual


class Button (posX : Int = 0, posY : Int = 0, width : Int = 0, height : Int = 0,
              text : String = "Button", fontSize : Int = 20) :
    Button(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        text = text,
        font = Font(size = fontSize, family = "Irish Grover"),
        visual = ImageVisual("button.png")
    )
{
        init {
            this.componentStyle="-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"
        }
    }
