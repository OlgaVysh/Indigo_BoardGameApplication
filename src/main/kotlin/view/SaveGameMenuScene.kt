package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class SaveGameMenuScene : MenuScene(1920, 1080, background = ImageVisual("SaveMenuScene.png")) {
    private val continueButton = Button(266, 642,528,207, "Continue game", 48)
    private val yesButton = Button(1100,642,528,207, "Yes", 48)
    private val label1 = Label(424, 284, 1072, 116, "Do you want to break of", 86)
    private val label2 = Label(424, 400, 1072, 116, "and save the game?", 86)
    init {
        opacity = 1.0
        addComponents(label1, label2, continueButton, yesButton)
    }
}