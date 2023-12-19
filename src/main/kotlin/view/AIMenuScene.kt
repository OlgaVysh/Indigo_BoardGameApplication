package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

class AIMenuScene (private val rootService: RootService) : MenuScene (1920, 1080) {
    private val game = rootService.currentGame

    private val aiGameLabel = Label (397, 71, 1126,155,"This is an AI Game!",120)
    private val speed1Label = Label (397, 324,1126,77,"Please, choose the simulation speed :",64)
    private val speed2Label = Label (397, 416,1192,58,"(recommended speed is : 1.0)",48)
    private val speed3Label = Label (230, 508,1192,58,"Set speed to :",48)

    private val aiSpeed = ComboBox<String>(1015, 495, 300, 69, prompt = "Select ai speed")


    private val startButton = Button(730, 805,532,207,"Start new game",48)

    init{
        background =  ImageVisual("ThreeGemsBackground.png")
        opacity = 1.0
        addComponents(
            aiGameLabel,
            speed1Label,
            speed2Label,
            startButton,
            speed3Label,
            aiSpeed)
    }

}