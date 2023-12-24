package service


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import createTestRouteTile
import entity.*


/**
 * Test class for testing the methods in GameService
 */
class GameServiceTest{

    var rootService : RootService = RootService()
    var gameService : GameService = rootService.gameService

    @BeforeEach
    fun setUp(){
        rootService = RootService()
        gameService = GameService(rootService)
    }

    @Test
    fun startGameTest(){
/*        assertNull(rootService.currentGame)
        val player1 = Player("Alice", Date(0),TokenColor.WHITE,false)
        val player2 = Player("Bob", Date(0),TokenColor.PURPLE,false)
        val player3 = Player("Emily", Date(0),TokenColor.BLUE,false)
        val player4 = Player("Jack", Date(0),TokenColor.RED,false)
        rootService.gameService.startGame(players = listOf(player1,player2,player3,player4))
        assertNotNull(rootService.currentGame)
        assertEquals(rootService.currentGame?.players, listOf(player1,player2,player3,player4))*/
    }
    @Test
    fun restartGameTest(){}
    @Test
    fun endGameTest(){}
    @Test
    fun checkPlacementTest(){
/*        val player1 = Player("Alice", Date(0),TokenColor.WHITE,false)
        val player2 = Player("Bob", Date(0),TokenColor.PURPLE,false)
        rootService.gameService.startGame(players = listOf(player1,player2))
        val testCoordinateGate = Coordinate(-2,4)

        val tile2test = Tile(
            listOf(
                Pair(Edge.ZERO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.TWO, Edge.THREE)
            ))
        val tile1Test = Tile(
            listOf(
                Pair(Edge.TWO, Edge.FIVE),
                Pair(Edge.ONE, Edge.FOUR),
                Pair(Edge.ZERO, Edge.THREE)
            ))
        //val placementCoordinate = rootService.currentGame?.gameBoard?.gameBoardTiles?.get(Coordinate(-2,4))
        assertFalse(rootService.gameService.checkPlacement(testCoordinateGate,tile2test))
        assertTrue(rootService.gameService.checkPlacement(testCoordinateGate,tile1test))*/
    }
    @Test
    fun checkCollisionTest(){}

    @Test
    fun saveGameTest(){}
    @Test
    fun loadGameTest(){}

    @Test
    fun changePlayerTest(){}
    @Test
    fun moveGemsTest(){}
    @Test
    fun removeGemsTest(){}

    @Test
    fun distributeNewTileTest(){}

    @Test
    fun initializeTilesTest(){}

    @Test
    fun initializeGemsTest(){}


}