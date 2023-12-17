package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class GameSavedMenuScene : MenuScene(1920, 1080) {
    private val hotSeatButton = Button(266, 642,528,207,"Exit",48)
    private val networkButton = Button(1100,642,528,207,"Start new game",48)
    private val label1 = Label(587, 271, 736,155,"Game saved!",120)


    init{
        background =  ImageVisual("SevenGems2Background.png")
        opacity=1.0
        addComponents(label1)
        addComponents(networkButton)
        addComponents(hotSeatButton)
    }
}