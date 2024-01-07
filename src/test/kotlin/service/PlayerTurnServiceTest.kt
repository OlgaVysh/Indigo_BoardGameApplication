package service

import org.junit.jupiter.api.Test
import entity.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest

class PlayerTurnServiceTest {

    private lateinit var rootService: RootService
    private lateinit var gameService: GameService
    private lateinit var playerTurnService: PlayerTurnService

    private val players = mutableListOf(
        Player(name = "ALice", color = TokenColor.RED),
        Player(name = "Bob", color = TokenColor.BLUE)
    )

    private val testTile = Tile(
        listOf(
            Pair(Edge.ZERO, Edge.TWO),
            Pair(Edge.ONE, Edge.FOUR),
            Pair(Edge.THREE, Edge.FIVE)
        ),
        mutableMapOf()
    )

    @BeforeTest
    fun setUp() {
        rootService =
            RootService()  // Stellen Sie sicher, dass Sie eine Instanz von RootService erstellen oder entsprechend initialisieren.
        gameService = GameService(rootService)
        playerTurnService = PlayerTurnService(rootService)
    }

    @Test
    fun testPlaceRouteTile() {
        // Hier können Sie Ihre Testlogik für placeRouteTile implementieren
        // zum Beispiel: Überprüfen Sie, ob das Platzieren einem gültigen Kacheln an einer gültigen Stelle funktioniert.

        // Beispiel:
        val tile = testTile
        testTile.gemEndPosition[2] = Gem(GemColor.AMBER)
        /* Initialisierung Ihrer Kachel für den Test*/
        val coordinate = Coordinate(-1, 1)
        assertThrows<IllegalStateException> { playerTurnService.placeRouteTile(coordinate, testTile) }
        rootService.gameService.startGame(players,true)
        assertThrows<Exception> { playerTurnService.placeRouteTile(Coordinate(0, 0), tile) }
        playerTurnService.rotateTileLeft(tile)
        playerTurnService.placeRouteTile(Coordinate(-1, 0), tile)
        var middleTileGem = rootService.currentGame!!.middleTile.gemPosition
        assertEquals(5, middleTileGem.size)
        var placedTile = rootService.currentGame!!.gameBoard.gameBoardTiles[Coordinate(-1, 0)]
        assertNotNull(placedTile)
        assertEquals(0, placedTile!!.gemEndPosition.size)
        assertEquals(10, rootService.currentGame!!.gems.size)

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
     *  The function [testUndoRedo] test the correctness of undo und redo
     *
     */
    @Test
    fun testUndoRedo() {
        assertThrows<IllegalStateException> { playerTurnService.redo() }
        assertThrows<IllegalStateException> { playerTurnService.undo() }
        // Hier können Sie Ihre Testlogik für undo und redo implementieren
        // zum Beispiel: Überprüfen Sie, ob das Undo und Redo wie erwartet funktioniert.
        gameService.startGame(players)
        val testGame = rootService.currentGame
        val player1HandTile = testGame!!.players[0].handTile
        println(player1HandTile.toString())
        assertNotNull(testGame)
        // Hier können Sie Aktionen ausführen, um das Spielzustand zu ändern
        // Führen Sie undo und redo durch
        playerTurnService.undo()
        playerTurnService.redo()

        playerTurnService.placeRouteTile(Coordinate(0, -1), testTile)
        var actualGame = rootService.currentGame
        val newPlayer1handTile = rootService.currentGame!!.players[0].handTile
        println(player1HandTile.toString())
        println(newPlayer1handTile.toString())
        assertNull(actualGame!!.nextGameState)
        assertNotNull(actualGame.previousGameState)
        assertEquals(testGame.gameBoard.gateTokens, actualGame.gameBoard.gateTokens)
        assertEquals(testGame.gameBoard.gameBoardTiles, actualGame.gameBoard.gameBoardTiles)
        assertEquals(testGame.gems, actualGame.gems)
        assertEquals(testGame.players.size,actualGame.players.size)
        for(i in testGame.players.indices){
            assertEquals(testGame.players[i].name,actualGame.players[i].name)
            assertEquals(testGame.players[i].handTile,actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter,actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].color,actualGame.players[i].color)
            assertEquals(testGame.players[i].handTile,actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter,actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].age,actualGame.players[i].age)
            assertEquals(testGame.players[i].isAI,actualGame.players[i].isAI)
            assertEquals(testGame.players[i].score,actualGame.players[i].score)
        }
        assertEquals(testGame.routeTiles,actualGame.routeTiles)
        assertEquals(51,actualGame.routeTiles.size)

        playerTurnService.undo()
        actualGame = rootService.currentGame
        assertNull(actualGame!!.previousGameState)
        assertEquals(0,actualGame.currentPlayerIndex)
        assertEquals(52,actualGame.routeTiles.size)
        assertEquals(player1HandTile,actualGame.players[0].handTile)
        assertEquals(6,actualGame.gameBoard.gameBoardTiles.size)
        assertEquals(6,actualGame.middleTile.gemPosition.size)

        playerTurnService.redo()
        actualGame = rootService.currentGame
        assertNull(actualGame!!.nextGameState)
        assertNotNull(actualGame.previousGameState)
        assertEquals(testGame.gameBoard.gateTokens, actualGame.gameBoard.gateTokens)
        assertEquals(testGame.gameBoard.gameBoardTiles, actualGame.gameBoard.gameBoardTiles)
        assertEquals(testGame.gems, actualGame.gems)
        assertEquals(newPlayer1handTile,actualGame.players[0].handTile)
        assertEquals(testGame.players.size,actualGame.players.size)
        for(i in testGame.players.indices){
            assertEquals(testGame.players[i].name,actualGame.players[i].name)
            assertEquals(testGame.players[i].handTile,actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter,actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].color,actualGame.players[i].color)
            assertEquals(testGame.players[i].handTile,actualGame.players[i].handTile)
            assertEquals(testGame.players[i].gemCounter,actualGame.players[i].gemCounter)
            assertEquals(testGame.players[i].age,actualGame.players[i].age)
            assertEquals(testGame.players[i].isAI,actualGame.players[i].isAI)
            assertEquals(testGame.players[i].score,actualGame.players[i].score)
        }
        assertEquals(51,actualGame.routeTiles.size)
        // Fügen Sie weitere Tests für andere Methoden hinzu
    }


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
        val expectedTile = testTile
        val expectedTileRightRotated = testTile
        expectedTile.edges.add(0, expectedTile.edges.removeAt(expectedTile.edges.size - 1))
        playerTurnService.rotateTileRight(testTile)
        assertEquals(expectedTileRightRotated, testTile)
        playerTurnService.rotateTileLeft(testTile)
        assertEquals(expectedTile, testTile)
    }
}
