package view
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

class HelloScene : BoardGameScene(1920, 1080) {

    private val button = Button(555,664, 810,207, fontSize = 50)

    private val helloLabel = Label(697,145,612,468, fontSize = 120)


    init {
        background =  ImageVisual("SevenGems1Background.png")
        addComponents(helloLabel)
        addComponents(button)
    }

}