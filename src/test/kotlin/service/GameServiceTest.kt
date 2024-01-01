package service

import createTestRouteTile
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

    private val tile0 = Tile(
        listOf(
            Pair(Edge.ZERO, Edge.TWO),
            Pair(Edge.ONE, Edge.FOUR),
            Pair(Edge.THREE, Edge.FIVE)
        )
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
        var testGame = rootService.currentGame
        assertNotNull(testGame)

        val player1 = Player("Alice", Date(0), TokenColor.WHITE, false)
        val player2 = Player("Bob", Date(0), TokenColor.PURPLE, false)
        val player3 = Player("Emily", Date(0), TokenColor.BLUE, false)
        val player4 = Player("Jack", Date(0), TokenColor.RED, false)
        val playerListe = mutableListOf(player1, player2, player3, player4)
        playerListe.toList()


        assertEquals(playerListe.size, testGame!!.players.size)
        for (i in playerListe.indices) {
            assertEquals(playerListe[i].name, testGame.players[i].name)
            assertEquals(playerListe[i].color, testGame.players[i].color)
            assertNotNull(testGame.players[i].handTile)
        }
        assertEquals(0, testGame.currentPlayerIndex)
        assertEquals(50, testGame.routeTiles.size)


        rootService.gameService.startGame(fourPlayers.toMutableList(), random = true)
        testGame = rootService.currentGame
        assertEquals(playerListe.size, testGame!!.players.size)
        assertNotEquals(fourPlayers.toMutableList(), testGame.players)
        for (i in playerListe.indices) {
            assertNotNull(testGame.players[i].handTile)
        }
    }

    /**
     * Test the restartGame function.
     */
    @Test
    fun restartGameTest() {
        assertNull(rootService.currentGame)
        rootService.gameService.restartGame(
            fourPlayers.toMutableList(), notSharedGate = false, random = false
        )
        var testGame = rootService.currentGame
        assertNotNull(rootService.currentGame)

        val player1 = Player("Alice", Date(0), TokenColor.WHITE, false)
        val player2 = Player("Bob", Date(0), TokenColor.PURPLE, false)
        val player3 = Player("Emily", Date(0), TokenColor.BLUE, false)
        val player4 = Player("Jack", Date(0), TokenColor.RED, false)
        val playerListe = mutableListOf(player1, player2, player3, player4)
        playerListe.toList()


        assertEquals(playerListe.size, testGame!!.players.size)
        for (i in playerListe.indices) {
            assertEquals(playerListe[i].name, testGame.players[i].name)
            assertEquals(playerListe[i].color, testGame.players[i].color)
            assertNotNull(testGame.players[i].handTile)
        }
        assertEquals(0, testGame.currentPlayerIndex)
        assertEquals(50, testGame.routeTiles.size)

        rootService.gameService.restartGame(fourPlayers.toMutableList(), notSharedGate = true, random = true)
        testGame = rootService.currentGame
        assertEquals(playerListe.size, testGame!!.players.size)
        assertNotEquals(fourPlayers.toMutableList(), testGame.players)
        for (i in playerListe.indices) {
            assertNotNull(testGame.players[i].handTile)
        }
    }

    /**
     * Test the endGame function.
     */
    @Test
    fun endGameTest() {
        assertThrows<IllegalStateException> { gameService.endGame() }
        gameService.startGame(fourPlayers.toMutableList())
        assertFalse(gameService.endGame())

        //no more gems in the field
        rootService.currentGame!!.gems.clear()
        assertTrue(gameService.endGame())

        //no more routeTiles in the game
        gameService.startGame(fourPlayers.toMutableList())
        rootService.currentGame!!.routeTiles.clear()
        repeat(4) {
            gameService.distributeNewTile()
            gameService.changePlayer()
        }
        assertTrue(gameService.endGame())

        //no more gems in the game and no more
        gameService.startGame(fourPlayers.toMutableList())
        rootService.currentGame!!.routeTiles.clear()
        rootService.currentGame!!.gems.clear()
        repeat(4) {
            gameService.distributeNewTile()
            gameService.changePlayer()
        }
        assertTrue(gameService.endGame())
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

        //rotate tile0 and place it in (-1,-3) ,dann check that the place is occupied for other tile.
        rootService.playerTurnService.rotateTileRight(tile0)
        assertFalse(rootService.gameService.checkPlacement(Coordinate(0, 0), tile4))
        assertTrue(rootService.gameService.checkPlacement(Coordinate(-1, -3), tile0))
        val exception1 = assertThrows<Exception> {
            rootService.gameService.checkPlacement(Coordinate(-1, -3), tile2)
        }
        assertEquals(exception1.message, "this place is occupied")

        //rotate tile2 and place it in (-2,-2) ,dann check that the gate is blocked, then rotate right and place it,then the place is occupied for other tile.
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

        //rotate tile4 and place it in (-3,-1) ,dann check that the gate is blocked, then rotate right und the place is occupied for other tile.
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
            mutableMapOf(Pair(1, Gem(EMERALD)), Pair(4, Gem(AMBER)))
        )
        assertTrue(rootService.gameService.checkCollision(tile0))
        //checkCollision for tile0 after removeGems
        assertFalse(rootService.gameService.checkCollision(tile0))

        //tileID 3 initialisieren and check collision.
        val tile3 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.THREE), Pair(Edge.TWO, Edge.FOUR)),
            mutableMapOf(Pair(1, Gem(EMERALD)), Pair(5, Gem(SAPPHIRE)))
        )
        assertFalse(rootService.gameService.checkCollision(tile3))
    }

    /**
     * Test the saveGame function.
     */

    @Test
    fun saveGameTest() {
        /*assertNull(rootService.currentGame)
        rootService.gameService.startGame(
            fourPlayers.toMutableList()
        )
        assertNotNull(rootService.currentGame)
        val filePath = "test/gameFile.json"
        rootService.gameService.saveGame(filePath)
        rootService.currentGame=null
        rootService.gameService.loadGame(filePath)
        assertNotNull(rootService.currentGame)*/
    }

    /**
     * Test the loadGame function.
     */
    @Test
    fun loadGameTest() {
        /* assertNull(rootService.currentGame)

         val Path = "test/gameFile1.json"
         rootService.gameService.loadGame(Path)
         assertNotNull(rootService.currentGame)*/
    }

    /**
     * Test the changePlayer function.
     */

    @Test
    fun changePlayerTest() {
        assertThrows<IllegalStateException> {
            rootService.gameService.changePlayer()
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
        val rootService = RootService()

        //tileID 0 initialisieren
        var tile0 = Tile(
            listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(AMBER)))
        )

        //tileID 2 initialisieren
        var tile2 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.TWO, Edge.THREE)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(SAPPHIRE)))
        )

        //tileID 4 initialisieren
        var tile4 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.TWO), Pair(Edge.THREE, Edge.FOUR)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(AMBER)))
        )

        assertThrows<IllegalStateException> { rootService.gameService.removeGemsReachedGate(tile0, Coordinate(4, -2)) }
        rootService.gameService.startGame(
            fourPlayers.toMutableList()
        )

        var indigo = rootService.currentGame
        checkNotNull(indigo)

        var players = indigo.players

        //gate4 no gems after the method because is removed
        rootService.gameService.removeGemsReachedGate(tile0, Coordinate(4, -2))
        assertEquals(0, tile0.gemEndPosition.size)
        assertEquals(2, players[3].gemCounter)
        assertEquals(3, players[3].score)
        assertEquals(2, players[1].gemCounter)
        assertEquals(3, players[1].score)
        assertEquals(10, rootService.currentGame!!.gems.size)
        //gate3 only one Gem is there
        rootService.gameService.removeGemsReachedGate(tile2, Coordinate(2, 2))
        assertEquals(1, tile2.gemEndPosition.size)
        assertEquals(3, players[3].gemCounter)
        assertEquals(5, players[3].score)
        assertEquals(1, players[0].gemCounter)
        assertEquals(2, players[0].score)

        //gate2 both gems are in the tile
        rootService.gameService.removeGemsReachedGate(tile4, Coordinate(-2, 4))
        assertEquals(2, tile4.gemEndPosition.size)
        assertEquals(0, players[2].gemCounter)
        assertEquals(0, players[2].score)

        val twoPlayers = listOf(
            Player("John", color = TokenColor.RED),
            Player("Jack", color = TokenColor.BLUE)
        )
        rootService.gameService.startGame(
            twoPlayers.toMutableList(), true
        )

        //tileID 0 initialisieren
        tile0 = Tile(
            listOf(Pair(Edge.ZERO, Edge.TWO), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.THREE, Edge.FIVE)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(AMBER)))
        )

        //tileID 2 initialisieren
        tile2 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.FOUR), Pair(Edge.TWO, Edge.THREE)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(SAPPHIRE)))
        )

        //tileID 4 initialisieren
        tile4 = Tile(
            listOf(Pair(Edge.ZERO, Edge.FIVE), Pair(Edge.ONE, Edge.TWO), Pair(Edge.THREE, Edge.FOUR)),
            mutableMapOf(Pair(2, Gem(EMERALD)), Pair(3, Gem(AMBER)))
        )


        indigo = rootService.currentGame
        checkNotNull(indigo)

        players = indigo.players

        //gate4 no gems after the method because is removed
        rootService.gameService.removeGemsReachedGate(tile0, Coordinate(4, -2))
        assertEquals(0, tile0.gemEndPosition.size)
        assertEquals(2, players[1].gemCounter)
        assertEquals(3, players[1].score)

        //gate3 only on Gem is there
        rootService.gameService.removeGemsReachedGate(tile2, Coordinate(2, 2))
        assertEquals(1, tile2.gemEndPosition.size)
        assertEquals(1, players[0].gemCounter)
        assertEquals(2, players[0].score)

        //gate2 both gems are in the tile
        rootService.gameService.removeGemsReachedGate(tile4, Coordinate(-2, 4))
        assertEquals(2, tile4.gemEndPosition.size)
        assertEquals(2, players[1].gemCounter)
        assertEquals(3, players[1].score)

    }

    /**
     * Test the distributeNewTile function.
     */
    @Test
    fun distributeNewTileTest() {
        assertThrows<IllegalStateException> { rootService.gameService.distributeNewTile() }
        val allTiles = mutableListOf<Tile>()
        for (i in 0 until 6) {
            val gemPos = (i + 3) % 6
            allTiles.add(
                Tile(
                    listOf(
                        Pair(
                            Edge.values()[(Edge.values().size + gemPos - 1) % 6],
                            Edge.values()[(Edge.values().size + gemPos + 1) % 6]
                        )
                    ), mutableMapOf(Pair(gemPos, Gem(AMBER)))
                )
            )
        }
        val routeTiles = createTestRouteTile()
        allTiles.addAll(routeTiles)
        val testSettings = GameSettings(fourPlayers)
        rootService.currentGame = Indigo(
            testSettings,
            GameBoard(),
            allTiles,
            RootService().gameService.initializeGems(),
            RootService().gameService.initializeTokens()
        )
        val testGame = rootService.currentGame
        testGame!!.gameBoard.gateTokens = createTestGateTokens(testGame, false)
        rootService.gameService.distributeNewTile()
        var testTile = testGame.players[0].handTile
        assertNotNull(testTile)
        assertEquals(tile0, testTile)
        testGame.routeTiles.clear()
        gameService.distributeNewTile()
        testTile = testGame.players[0].handTile
        assertNull(testTile)
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
        val amber = AMBER
        val emerald = EMERALD
        val sapphire = SAPPHIRE

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

        val amountBlue = game.tokens.count { it == Token(blue) }
        val amountPurple = game.tokens.count { it == Token(purple) }
        val amountWhite = game.tokens.count { it == Token(white) }
        val amountRed = game.tokens.count { it == Token(red) }
        assertEquals(6, amountRed)
        assertEquals(6, amountWhite)
        assertEquals(6, amountBlue)
        assertEquals(6, amountPurple)
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

