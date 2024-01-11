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


    private val startScene = NewGameMenuScene(this)
    public val modusScene = ModusMenuScene()
    public val gatesScene = GateMenuScene()
    public val networkScene = NetworkMenuScene()
    public val saveGameScene = SaveGameMenuScene()
    public val gameSavedMenuScene = GameSavedMenuScene()
    public val newPlayerMenuScene = NewPlayerMenuScene(rootService)
    public val configurePlayerXScene = ConfigurePlayerXScene(rootService)
    public val joinGameScene = JoinGameScene(rootService)
    public val endGameMenuScene = EndGameMenuScene(rootService)
    public val aiMenuScene = AIMenuScene(rootService)
    public val hostGameScene = HostGameScene(rootService)
    public val savedGamesScene = SavedGamesMenuScene(listOf("one","two","three"))
    public val networkConfigureScene = ConfigureNetworkPlayersScene(listOf("one","two","three"))
    public val gameScene = GameScene()



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