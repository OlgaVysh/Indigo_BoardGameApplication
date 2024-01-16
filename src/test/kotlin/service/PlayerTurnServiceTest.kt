package service

import org.junit.jupiter.api.Test
import entity.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest

/**
 * Unit tests for the [PlayerTurnService] class.
 */
class PlayerTurnServiceTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerTurnService: PlayerTurnService
    // Sample players for testing

    private val players = mutableListOf(
        Player(name = "ALice", color = TokenColor.RED),
        Player(name = "Bob", color = TokenColor.BLUE)
    )
    // Sample test tile with specific edge configurations

    private val testTile = Tile(
        listOf(
            Pair(Edge.ZERO, Edge.TWO),
            Pair(Edge.ONE, Edge.FOUR),
            Pair(Edge.THREE, Edge.FIVE)
        ),TileType.Type_0,
        mutableMapOf()
    )

    /**
     * Set up the test environment before each test case.
     */
    @BeforeTest
    fun setUp() {
        // Initialize the necessary services and dependencies
        rootService =
            RootService()
        gameService = GameService(rootService)
        playerTurnService = PlayerTurnService(rootService)
    }

    /**
     * Test the functionality of placing a route tile on the game board.
     */
    @Test
    fun testPlaceRouteTile() {
        // Initialize a test tile with specific configurations, including gem positions

        val tile = testTile
        testTile.gemEndPosition[2] = Gem(GemColor.AMBER)
        // Attempt to place the tile at an invalid coordinate and expect an IllegalStateException
        val coordinate = Coordinate(-1, 1)
        assertThrows<IllegalStateException> { playerTurnService.placeRouteTile(coordinate, testTile) }
        // Start a game and attempt to place the tile at another invalid coordinate, expecting an exception
        rootService.gameService.startGame(players, true)
        assertThrows<Exception> { playerTurnService.placeRouteTile(Coordinate(0, 0), tile) }
        playerTurnService.rotateTileLeft(tile)
        // Rotate the tile left, place it at a valid coordinate, and verify the changes in the game state

        playerTurnService.placeRouteTile(Coordinate(-1, 0), tile)
        var middleTileGem = rootService.currentGame!!.middleTile.gemPosition
        assertEquals(5, middleTileGem.size)
        var placedTile = rootService.currentGame!!.gameBoard.gameBoardTiles[Coordinate(-1, 0)]
        assertNotNull(placedTile)
        assertEquals(0, placedTile!!.gemEndPosition.size)
        assertEquals(10, rootService.currentGame!!.gems.size)
        // Rotate the tile right, modify its gem configuration, place it at another valid coordinate, and verify changes

        playerTurnService.rotateTileRight(tile)
        tile.gemEndPosition.clear()
        tile.gemEndPosition[4] = Gem(GemColor.AMBER)
        playerTurnService.placeRouteTile(Coordinate(0, -1), tile)
        middleTileGem = rootService.currentGame!!.middleTile.gemPosition
        assertEquals(4, middleTileGem.size)
        placedTile = rootService.currentGame!!.gameBoard.gameBoardTiles[Coordinate(0, -1)]
        assertNotNull(placedTile)
        assertEquals(0, placedTile!!.gemEndPosition.size)
        assertEquals(8, rootService.currentGame!!.gems.size)
    }

    /**
     * Test the correctness of undo and redo operations.
     */
    @Test
    fun testUndoRedo() {
        // Check that redo and undo operations throw IllegalStateException
        assertThrows<IllegalStateException> { playerTurnService.redo() }
        assertThrows<IllegalStateException> { playerTurnService.undo() }
        // Initialize game and get the initial player's hand tile
        gameService.startGame(players)
        val testGame = rootService.currentGame
        val player1HandTile = testGame!!.players[0].handTile
        println(player1HandTile.toString())
        assertNotNull(testGame)
        // Perform actions to change the game state and then undo and redo
        playerTurnService.undo()
        playerTurnService.redo()
        // Place a route tile and observe the changes in the game state
        playerTurnService.placeRouteTile(Coordinate(0, -1), testTile)
        var actualGame = rootService.currentGame
        val newPlayer1handTile = rootService.currentGame!!.players[0].handTile
        println(player1HandTile.toString())
        println(newPlayer1handTile.toString())
        assertNull(actualGame!!.nextGameState)
        assertNotNull(actualGame.previousGameState)
        // Validate the consistency of the game state after placing a route tile
        assertEquals(testGame.gameBoard.gateTokens, actualGame.gameBoard.gateTokens)
        assertEquals(testGame.gameBoard.gameBoardTiles, actualGame.gameBoard.gameBoardTiles)
        assertEquals(testGame.gems, actualGame.gems)
        assertEquals(testGame.players.size, actualGame.players.size)
        // Loop through player details to ensure consistency
        for (i in testGame.players.indices) {
            assertEquals(testGame.players[i].name, actualGame.players[i].name)
            assertEquals(testGame.players[i].handTile, actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter, actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].color, actualGame.players[i].color)
            assertEquals(testGame.players[i].handTile, actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter, actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].age, actualGame.players[i].age)
            assertEquals(testGame.players[i].isAI, actualGame.players[i].isAI)
            assertEquals(testGame.players[i].score, actualGame.players[i].score)
        }
        // Validate route tiles and their count
        assertEquals(testGame.routeTiles, actualGame.routeTiles)
        assertEquals(51, actualGame.routeTiles.size)
        // Undo the last action and observe the changes in the game state

        playerTurnService.undo()
        actualGame = rootService.currentGame
        assertNull(actualGame!!.previousGameState)
        assertEquals(0, actualGame.currentPlayerIndex)
        assertEquals(52, actualGame.routeTiles.size)
        assertEquals(player1HandTile, actualGame.players[0].handTile)
        assertEquals(6, actualGame.gameBoard.gameBoardTiles.size)
        assertEquals(6, actualGame.middleTile.gemPosition.size)
        // Redo the last undone action and validate the game state

        playerTurnService.redo()
        actualGame = rootService.currentGame
        assertNull(actualGame!!.nextGameState)
        assertNotNull(actualGame.previousGameState)
        assertEquals(testGame.gameBoard.gateTokens, actualGame.gameBoard.gateTokens)
        assertEquals(testGame.gameBoard.gameBoardTiles, actualGame.gameBoard.gameBoardTiles)
        assertEquals(testGame.gems, actualGame.gems)
        assertEquals(newPlayer1handTile, actualGame.players[0].handTile)
        assertEquals(testGame.players.size, actualGame.players.size)
        // Loop through player details to ensure consistency after redo
        for (i in testGame.players.indices) {
            assertEquals(testGame.players[i].name, actualGame.players[i].name)
            assertEquals(testGame.players[i].handTile, actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter, actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].color, actualGame.players[i].color)
            assertEquals(testGame.players[i].handTile, actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter, actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].age, actualGame.players[i].age)
            assertEquals(testGame.players[i].isAI, actualGame.players[i].isAI)
            assertEquals(testGame.players[i].score, actualGame.players[i].score)
        }
        // Validate route tiles and their count after redo
        assertEquals(51, actualGame.routeTiles.size)
    }


    /**
     * Tests the undo functionality by checking the game state after undoing a player's move.
     * It involves starting a game, placing a route tile, undoing the move, and verifying the game state consistency.
     */
    @Test
    fun testUndo() {

        // Start the game and get the initial player's hand tile
        gameService.startGame(players)
        val testGame = rootService.currentGame
        val initialPlayer1HandTile = testGame!!.players[0].handTile
        assertNotNull(initialPlayer1HandTile)

        // Print initial hand tile
        println("Initial Player 1 Hand Tile: $initialPlayer1HandTile")

        // Place the initial tile
        playerTurnService.placeRouteTile(Coordinate(0, -1), initialPlayer1HandTile!!)
        var actualGame = rootService.currentGame
        assertNotNull(actualGame)

        // Print hand tile after placing route tile
        println("Player 1 Hand Tile after placing route tile: ${actualGame!!.players[0].handTile}")

        // Undo the move
        playerTurnService.undo()
        actualGame = rootService.currentGame


        // Get the updated hand tile after undo
        val updatedPlayer1HandTile = actualGame!!.players[0].handTile

        // Assertions after undo
        println("Player 1 Hand Tile after undo: $updatedPlayer1HandTile")
        assertEquals(initialPlayer1HandTile.paths, updatedPlayer1HandTile?.paths)

    }

    /**
     *  The function [rotateTileTest] the function rotate
     */
    @Test
    fun rotateTileTest() {
        // Set up the expected and initial tile configurations
        val expectedTile = testTile
        val expectedTileRightRotated = testTile
        expectedTile.edges.add(0, expectedTile.edges.removeAt(expectedTile.edges.size - 1))
        // Rotate the tile to the right and check the result
        playerTurnService.rotateTileRight(testTile)
        assertEquals(expectedTileRightRotated, testTile)
        // Rotate the tile back to its original position and check
        playerTurnService.rotateTileLeft(testTile)
        assertEquals(expectedTile, testTile)
    }
}
