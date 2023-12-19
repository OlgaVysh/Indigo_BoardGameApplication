package view

import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

class EndGameMenuScene (private val rootService: RootService) : MenuScene(1920, 1080) {
    private val game = rootService.currentGame

    private val gameOverLabel = Label(650, 48, 620, 155, "Game Over", 120)

    private val exitButton = Button(185, 780, 532, 207, "Exit", 48)
    private val newGameButton = Button(1180, 780, 532, 207, "Start new game", 48)

    init {
        background = ImageVisual("FiveGemsBackGround.png")
        opacity = 1.0
        addComponents(
            gameOverLabel,
            exitButton,
            newGameButton)

    }

}