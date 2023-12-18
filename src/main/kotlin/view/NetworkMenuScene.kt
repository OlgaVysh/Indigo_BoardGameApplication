package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class NetworkMenuScene : MenuScene(1920, 1080,  background = ImageVisual("SevenGems2Background.png")) {
    private val hostButton = Button(266, 642,528,207, "Host game", 48)
    private val joinButton = Button(1100,642,528,207, "Join game", 48)
    private val networkLabel1 = Label(381, 370, 1111, 85, "Please, choose one of the following", 60)
    private val networkLabel2 = Label(381, 469, 1111, 85, "options :", 60)
    init {
        opacity = 1.0
        addComponents(networkLabel1, networkLabel2, hostButton, joinButton)
    }
}