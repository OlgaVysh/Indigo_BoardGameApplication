package view
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class GateMenuScene : MenuScene(1920, 1080) {
    private val sharedButton = Button(266, 642,528,207, "HotSeat", 48)
    private val separatedButton = Button(1100,642,528,207, "Network", 48)
    private val gatesLabel1 = Label(381, 370, 1111, 85, "Please, choose one of the following", 60)
    private val gatesLabel2 = Label(381, 469, 1111, 85, "options :", 60)
    init {
        background = ImageVisual("SevenGems2Background.png")
        opacity = 1.0
        addComponents(gatesLabel1)
        addComponents(gatesLabel2)
        addComponents(sharedButton)
        addComponents(separatedButton)
    }
}