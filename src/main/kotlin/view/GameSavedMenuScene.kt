package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

class GameSavedMenuScene : MenuScene(1920, 1080, background =  ImageVisual("SevenGems2Background.png")) {
    private val hotSeatButton = Button(266, 642,528,207,"Exit",48)
    private val networkButton = Button(1100,642,528,207,"Start new game",48)
    private val label1 = Label(587, 271, 736,155,"Game saved!",120)


    init{
        opacity=1.0
        addComponents(label1, networkButton, hotSeatButton)
    }
}