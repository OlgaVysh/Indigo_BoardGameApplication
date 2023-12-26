package service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import entity.*
import entity.GemColor.EMERALD


/**
 * Test class for testing the methods in GameService
 */
class GameServiceTest {

    private var rootService: RootService = RootService()
    private var gameService: GameService = rootService.gameService

    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
    }

    @Test
    fun startGameTest() {
        assertNull(rootService.currentGame)

        rootService.gameService.startGame(
            player1 = Player("Alice", Date(0), TokenColor.WHITE, false),
            player2 = Player("Bob", Date(0), TokenColor.PURPLE, false),
            player3 = Player("Emily", Date(0), TokenColor.BLUE, false),
            player4 = Player("Jack", Date(0), TokenColor.RED, false)
        )

        val player1 = Player("Alice", Date(0), TokenColor.WHITE, false)
        val player2 = Player("Bob", Date(0), TokenColor.PURPLE, false)
        val player3 = Player("Emily", Date(0), TokenColor.BLUE, false)
        val player4 = Player("Jack", Date(0), TokenColor.RED, false)
        val playerListe = mutableListOf(player1, player2, player3, player4)
        playerListe.toList()
        assertNotNull(rootService.currentGame)
        assertEquals(rootService.currentGame?.players, playerListe)
    }

    @Test
    fun restartGameTest() {
    }

    @Test
    fun endGameTest() {
    }

    @Test
    fun checkPlacementTest() {
        val game = rootService.gameService.startGame()
        val indigo = rootService.currentGame

        checkNotNull(indigo)

        //tileID 0 initialisieren und testen
        val tile0 = Tile(
            listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )
        rootService.playerTurnService.rotateTileRight(tile0)
        rootService.playerTurnService.rotateTileRight(tile0)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile0))
        rootService.playerTurnService.rotateTileLeft(tile0)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile0))
        assertTrue(rootService.gameService.checkPlacement(Coordinate(1, -4), tile0))

        //tileID 2 initialisieren und testen

        val tile2 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.TWO, Edge.THREE)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )
        assertFalse(rootService.gameService.checkPlacement(Coordinate(1, -4), tile2))
        rootService.playerTurnService.rotateTileLeft(tile2)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(1, -4), tile2))
        rootService.playerTurnService.rotateTileLeft(tile2)
        rootService.playerTurnService.rotateTileLeft(tile2)
        assertFalse(rootService.gameService.checkPlacement(Coordinate(1, -4), tile2))
        assertTrue(rootService.gameService.checkPlacement(Coordinate(4, -2), tile2))
        assertTrue(rootService.gameService.checkPlacement(Coordinate(2, 2), tile2))

        //tileID 4 initialisieren und testen
        val tile4 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.TWO), Pair(Edge.THREE, Edge.FOUR)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )
        assertFalse(rootService.gameService.checkPlacement(Coordinate(1, -4), tile4))
        rootService.playerTurnService.rotateTileLeft(tile4)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(1, -4), tile4))

        assertFalse(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile4))
        rootService.playerTurnService.rotateTileRight(tile4)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile4))


    }

    @Test
    fun checkCollisionTest() {
    }

    @Test
    fun saveGameTest() {
    }

    @Test
    fun loadGameTest() {
    }

    @Test
    fun changePlayerTest() {
    }

    @Test
    fun moveGemsTest() {
    }

    @Test
    fun removeGemsTest() {
    }

    @Test
    fun distributeNewTileTest() {
    }

    @Test
    fun initializeTilesTest() {
    }

    @Test
    fun initializeGemsTest() {
    }


}