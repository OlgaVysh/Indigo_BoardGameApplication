package service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import entity.*
import entity.GemColor.*
import org.junit.jupiter.api.*


/**
 * Test class for testing the methods in GameService
 */
class GameServiceTest {

    private var rootService: RootService = RootService()
    private var gameService: GameService = rootService.gameService

    private val fourPlayers = listOf(
        Player("Alice", Date(0), TokenColor.WHITE, false),
        Player("Bob", Date(0), TokenColor.PURPLE, false),
        Player("Emily", Date(0), TokenColor.BLUE, false),
        Player("Jack", Date(0), TokenColor.RED, false)
    )

    /**
     * Set up method executed before each test.
     */
    @BeforeEach
    fun setUp() {
        rootService = RootService()
        gameService = GameService(rootService)
    }

    /**
     * Test the startGame function to ensure a new game is correctly initialized.
     */
    @Test
    fun startGameTest() {
        assertNull(rootService.currentGame)
        rootService.gameService.startGame(
            fourPlayers.toMutableList()
        )

        val player1 = Player("Alice", Date(0), TokenColor.WHITE, false)
        val player2 = Player("Bob", Date(0), TokenColor.PURPLE, false)
        val player3 = Player("Emily", Date(0), TokenColor.BLUE, false)
        val player4 = Player("Jack", Date(0), TokenColor.RED, false)
        val playerListe = mutableListOf(player1, player2, player3, player4)
        playerListe.toList()
        val testGame = rootService.currentGame
        assertNotNull(rootService.currentGame)

        assertEquals(playerListe.size, testGame!!.players.size)
        for (i in playerListe.indices) {
            assertEquals(playerListe[i].name, testGame.players[i].name)
            assertEquals(playerListe[i].color, testGame.players[i].color)
        }
        assertEquals(50,testGame.routeTiles.size)
    }

    /**
     * Test the restartGame function.
     */
    @Test
    fun restartGameTest() {
    }

    /**
     * Test the endGame function.
     */
    @Test
    fun endGameTest() {
    }

    /**
     * Test the checkPlacement function.
     */
    @Test
    fun checkPlacementTest() {
        val rootService = RootService()
        assertNull(rootService.currentGame)
        rootService.gameService.startGame(
        )

        val indigo = rootService.currentGame

        checkNotNull(indigo)

        //tileID 0 initialisieren
        val tile0 = Tile(
            listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )

        //tileID 2 initialisieren
        val tile2 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.TWO, Edge.THREE)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )

        //tileID 4 initialisieren
        val tile4 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.TWO), Pair(Edge.THREE, Edge.FOUR)),
            mutableMapOf(Pair(1, Gem(EMERALD)))
        )

        //rotate tile0 and place it in (-1,-3) ,dann check that the place is ocuppied for other tile.
        rootService.playerTurnService.rotateTileRight(tile0)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile0))
        val exception1 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-1, -3), tile2)
        }
        assertEquals(exception1.message, "this place is occupied")

        //rotate tile2 and place it in (-2,-2) ,dann check that the gate is blocked, then rotate right and place it,then the place is ocuppied for other tile.
        rootService.playerTurnService.rotateTileLeft(tile2)
        val exception2 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-2, -2), tile2)
        }
        assertEquals(exception2.message, "tile blocks exit, please rotate Tile")
        rootService.playerTurnService.rotateTileRight(tile2)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-2, -2), tile2))
        val exception3 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-2, -2), tile4)
        }
        assertEquals(exception3.message, "this place is occupied")

        //rotate tile4 and place it in (-3,-1) ,dann check that the gate is blocked, then rotate right und the place is ocuppied for other tile.
        rootService.playerTurnService.rotateTileLeft(tile4)
        val exception4 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-3, -1), tile4)
        }
        assertEquals(exception4.message, "tile blocks exit, please rotate Tile")

        rootService.playerTurnService.rotateTileRight(tile4)
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-3, -1), tile4))
        val exception5 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-3, -1), tile0)
        }
        assertEquals(exception5.message, "this place is occupied")

    }


    /**
     * Test the checkCollision function.
     */
    @Test
    fun checkCollisionTest() {
        val rootService = RootService()
        assertNull(rootService.currentGame)
        rootService.gameService.startGame(
        )

        val indigo = rootService.currentGame
        checkNotNull(indigo)

        //tileID 0 initialisieren and check collision.
        val tile0 = Tile(
            listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
            mutableMapOf(Pair(1, Gem(GemColor.EMERALD)), Pair(4, Gem(GemColor.AMBER)))
        )
        assertTrue(rootService.gameService.checkCollision(tile0))
        //checkCollision for tile0 after removeGems
        assertFalse(rootService.gameService.checkCollision(tile0))

        //tileID 3 initialisieren and check collision.
        val tile3 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.THREE), Pair(Edge.TWO, Edge.FOUR)),
            mutableMapOf(Pair(1, Gem(GemColor.EMERALD)), Pair(5, Gem(GemColor.SAPPHIRE)))
        )
        assertFalse(rootService.gameService.checkCollision(tile3))
    }

    /**
     * Test the saveGame function.
     */

    @Test
    fun saveGameTest() {
    }

    /**
     * Test the loadGame function.
     */
    @Test
    fun loadGameTest() {
    }

    /**
     * Test the changePlayer function.
     */

@Test
fun changePlayerTest() {
    assertThrows<IllegalStateException> {
        rootService.gameService.changePlayer( )
    }
    rootService.gameService.startGame(fourPlayers.toMutableList())
    val testGame = rootService.currentGame
    checkNotNull(testGame)
    rootService.gameService.changePlayer()
    var currentPlayerIndex = testGame.currentPlayerIndex
    assertEquals(1, currentPlayerIndex)
    repeat(3) {
        rootService.gameService.changePlayer()
    }
    currentPlayerIndex = testGame.currentPlayerIndex
    assertEquals(0, currentPlayerIndex)
}

    /**
     * Test the moveGems function.
     */
    @Test
    fun moveGemsTest() {
    }

    /**
     * Test the removeGems function.
     */
    @Test
    fun removeGemsReachedGateTest() {
        /* val rootService = RootService()
         assertNull(rootService.currentGame)
         rootService.gameService.startGame(
         )

         val indigo = rootService.currentGame
         checkNotNull(indigo)

         //tileID 0 initialisieren and check collision.
         val tile0 = Tile(
             listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
             mutableMapOf(Pair(1, Gem(EMERALD)))
         )*/
    }

    /**
     * Test the distributeNewTile function.
     */
    @Test
    fun distributeNewTileTest() {
    }

    /**
     * Test the initializeGems function.
     */

    @Test
    fun initializeGemsTest() {
        assertNull(rootService.currentGame)
        rootService.gameService.startGame(
            fourPlayers.toMutableList()
        )
        val game = rootService.currentGame
        checkNotNull(game)
        val amber = GemColor.AMBER
        val emerald = GemColor.EMERALD
        val sapphire = GemColor.SAPPHIRE

        for (i in game.gems.indices) {
            if (i in 0 until 6) {
                assertEquals(amber, game.gems[i].gemColor)
            }
            if (i in 6 until 11) {
                assertEquals(emerald, game.gems[i].gemColor)
            }
            assertEquals(sapphire, game.gems[game.gems.size - 1].gemColor)
        }
    }

    @Test
    fun initializeTokenTest() {
        assertNull(rootService.currentGame)
        rootService.gameService.startGame(
            fourPlayers.toMutableList()
        )
        val game = rootService.currentGame
        checkNotNull(game)
        val white = TokenColor.WHITE
        val purple = TokenColor.PURPLE
        val blue = TokenColor.BLUE
        val red = TokenColor.RED

        for (i in game.tokens.indices) {
            if (i in 0 until 6) {
                assertEquals(white, game.tokens[i].color)
            }
            if (i in 6 until 12) {
                assertEquals(purple, game.tokens[i].color)
            }
            if (i in 12 until 18) {
                assertEquals(blue, game.tokens[i].color)
            }
            if (i in 18 until 24) {
                assertEquals(red, game.tokens[i].color)
            }
        }
    }

}

