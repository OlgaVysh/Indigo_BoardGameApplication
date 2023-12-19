package view
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

class HelloScene : BoardGameScene(1920, 1080,  background =  ImageVisual("SevenGems1Background.png")) {

    private val button = Button(555,664, 810,207, fontSize = 50)

    private val helloLabel = Label(697,145,612,468, fontSize = 120)


    init {
        addComponents(helloLabel)
        addComponents(button)
    }

}