package service
import entity.*
import java.io.File
import kotlin.test.*

/**
 * This class contains test cases for the [GameService.saveGame]  functions.
 * */
class SavegameTest {
    private lateinit var gameService: GameService
    private lateinit var playerTurnService: PlayerTurnService
    private lateinit var rootService: RootService

    @BeforeTest
    fun setUp() {
        rootService = RootService()
        playerTurnService = PlayerTurnService(rootService)
        gameService = GameService(rootService)    }
    @AfterTest
    fun tearDown() {
        File("saveGame.ser").delete()
    }
    @Test
    fun saveTest() {
        val players = mutableListOf<Player>()
        gameService.startGame(players, false, false)
        val file = "/Users/mohammadkarkanawi/IdeaProjects/Projekt2-gruppe1/src/test/resources/"

        gameService.saveGame(file)
        assert(File(file).exists())
        assert(File(file).readText().isNotEmpty())    }

}
