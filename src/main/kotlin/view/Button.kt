package view
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class Button (posX : Int = 0, posY : Int = 0, text : String = "Button") :
    Button(
        posX = posX,
        posY = posY,
        width = 300,
        height = 85,
        text = text,
        font = Font(size = 20, color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("button.png").apply {  }
    )