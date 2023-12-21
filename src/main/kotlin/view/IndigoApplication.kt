package view

import service.RootService
import tools.aqua.bgw.core.BoardGameApplication
import java.io.File
import java.io.FileNotFoundException

/**
 * Implementation of the BGW [BoardGameApplication] for the example game "Indigo"
 */

class IndigoApplication : BoardGameApplication("Indigo Game") //,Refreshable{
{
    // Central service from which all others are created/accessed
    // also holds the currently active game
    private val rootService = RootService()


    private val startScene = NewGameMenuScene()
   /* private val modusScene = ModusMenuScene()
    private val gatesScene = GateMenuScene()
    private val networkScene = NetworkMenuScene()
    private val saveGameScene = SaveGameMenuScene()
    private val gameSavedMenuScene = GameSavedMenuScene()
    private val newPlayerMenuScene = NewPlayerMenuScene(rootService)
    private val configurePlayerXScene = ConfigurePlayerXScene(rootService)
    private val joinGameScene = JoinGameScene(rootService)
    private val endGameMenuScene = EndGameMenuScene(rootService)
    private val aiMenuScene = AIMenuScene(rootService)
    private val hostGameScene = HostGameScene(rootService)
    private val savedGamesScene = SavedGamesMenuScene(listOf("one","two","three"))
    private val networkConfigureScene = ConfigureNetworkPlayersScene(listOf("one","two","three"))
    private val gameScene = GameScene(rootService) */



    init {
        //das ladet unser IrishGrover Font
        val resource = this::class.java.getResource("/IrishGrover.ttf")
            ?: throw FileNotFoundException()
        val fontFile = File(resource.toURI())
        loadFont(fontFile)

        this.showMenuScene(startScene)

        //Testen der GameScene
       // this.showGameScene(gameScene)
    }


    //In jeder Szene : private val gradient ="-fx-text-fill: linear-gradient(to bottom, #061598, #06987E);"
    //an jedem Component mit Text : font = Font(family: "Irish Grover")
    // .apply { componentStyle = gradient }


}