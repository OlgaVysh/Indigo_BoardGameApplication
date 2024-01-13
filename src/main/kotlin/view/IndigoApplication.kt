package view


import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import java.io.File
import java.io.FileNotFoundException


/**
 * Implementation of the BGW [BoardGameApplication] for the example game "Indigo"
 */

class IndigoApplication : BoardGameApplication("Indigo Game"), Refreshable {

    // Central service from which all others are created/accessed
    // also holds the currently active game


    val rootService = RootService()

    val startScene = NewGameMenuScene(this)
    val modusScene = ModusMenuScene(this)
    val configurePlayersScene = ConfigurePlayersGameScene(this)
    //val gatesScene = GateMenuScene(this)
     val networkScene = NetworkMenuScene(this)
     val configurePlayerXScene = ConfigurePlayerXScene(this)
     val joinGameScene = JoinGameScene(this)
     //val endGameMenuScene = EndGameMenuScene(this)
     // val aiMenuScene = AIMenuScene(this)
    val gameSavedScene = GameSavedMenuScene(this)
    val saveGameScene = SaveGameMenuScene(this)
    val hostGameScene = HostGameScene(this)
    val savedGamesScene = SavedGamesMenuScene(listOf("one", "two", "three"))
    val networkConfigureScene = ConfigureNetworkPlayersScene(this, listOf("one", "two", "three"))
    val gameScene = GameScene(this)
    //val newPlayerScene = NewPlayerScene (this)


    init {
        //das ladet unser IrishGrover Font
        val resource = this::class.java.getResource("/IrishGrover.ttf")
            ?: throw FileNotFoundException()
        val fontFile = File(resource.toURI())
        loadFont(fontFile)

        this.showMenuScene(startScene)

        //Testen der GameScene
        //this.showGameScene(gameScene)
    }


    //In jeder Szene : private val gradient ="-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"
    //an jedem Component mit Text : font = Font(family: "Irish Grover")
    // .apply { componentStyle = gradient }


}