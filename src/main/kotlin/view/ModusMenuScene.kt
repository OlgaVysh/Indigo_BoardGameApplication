package view
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class ModusMenuScene : MenuScene(1920, 1080, background =  ImageVisual("SevenGems2Background.png")) {
    private val hotSeatButton = Button(266, 642,528,207,"HotSeat",48)
    private val networkButton = Button(1100,642,528,207,"Network",48)
    private val modusLabel1 = Label(639, 388, 642,85,"Please, choose your",60)
    private val modusLabel2 = Label(639, 455, 642,85,"game mode:",60)

    init{
        opacity=1.0
        addComponents(modusLabel1, modusLabel2, networkButton, hotSeatButton)
    }
}