package view

import tools.aqua.bgw.core.BoardGameApplication
import java.io.File
import java.io.FileNotFoundException

/**
 * Implementation of the BGW [BoardGameApplication] for the example game "Indigo"
 */

class IndigoApplication : BoardGameApplication("Indigo Game") //,Refreshable{
{
    private val helloScene = HelloScene()
    init {
        //das ladet unser IrishGrover Font
        val resource = this::class.java.getResource("/IrishGrover.ttf")
            ?: throw FileNotFoundException()
        val fontFile = File(resource.toURI())
        loadFont(fontFile)

        this.showGameScene(helloScene)
    }


    //In jeder Szene : private val gradient ="-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"
    //an jedem Component mit Text : font = Font(family: "Irish Grover")
    // .apply { componentStyle = gradient }

}