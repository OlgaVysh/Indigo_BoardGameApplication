package view
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual

class NewGameMenuScene : MenuScene(1920, 1080,  background =  ImageVisual("NewGameMenuBackground.png")) {

    private val startButton = Button(696, 337,528,207,"Start new game",48)

    private val continueButton = Button(696,664,528,207,"Continue game",48)
    private val indigoLabel = Label(775, 62, 370,155,"Indigo",120)

    init{
        opacity = 1.0
        addComponents(indigoLabel, startButton, continueButton)
    }

}