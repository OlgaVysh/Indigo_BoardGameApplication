package view

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class HelloScene : BoardGameScene(1920, 1080) {

    private val gradient ="-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"

    private val helloLabel = Label(
        width = 697,
        height = 145,
        posX = 612,
        posY = 468,
        text = "Indigo Game",
        font = Font(size = 100, family = "Irish Grover")
    ).apply{componentStyle = gradient}

    init {
        background =  ImageVisual("SevenGems1Background.png")
        addComponents(helloLabel)
    }

}