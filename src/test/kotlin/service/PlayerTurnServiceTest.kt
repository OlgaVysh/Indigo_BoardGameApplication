package service

import org.junit.jupiter.api.Test
import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
    /*
    @Test
    fun testPlaceRouteTile() {
        // Hier können Sie Ihre Testlogik für placeRouteTile implementieren
        // Zum Beispiel: Überprüfen Sie, ob das Platzieren eines gültigen Kacheln an einer gültigen Stelle funktioniert.

        // Beispiel:
        val tile = Tile
        /* Initialisierung Ihrer Kachel für den Test*/)
        val coordinate = Coordinate(-1,1 )

        try {
            playerTurnService.placeRouteTile(coordinate, tile, 3,0)
            // Hier können Sie Assertions platzieren, um sicherzustellen, dass die Operation erfolgreich war.
        } catch (e: Exception) {
            // Wenn Sie erwarten, dass eine Ausnahme geworfen wird, können Sie sie hier erfassen.
            // Beispiel: fail("Erwartete keine Ausnahme, aber es wurde eine geworfen.")
        }
    }
    */
    /**
     *  The function [testUndoRedo] test the correctness of undo und redo
     *
     */
    @Test
    fun testUndoRedo() {
        assertThrows<IllegalStateException> { playerTurnService.redo() }
        assertThrows<IllegalStateException> { playerTurnService.undo() }
        // Hier können Sie Ihre Testlogik für undo und redo implementieren
        // Zum Beispiel: Überprüfen Sie, ob das Undo und Redo wie erwartet funktioniert.
        gameService.startGame(players)
        val testGame = rootService.currentGame
        assertNotNull(testGame)
        // Hier können Sie Aktionen ausführen, um das Spielzustand zu ändern
        // Führen Sie undo und redo durch
        playerTurnService.undo()
        playerTurnService.redo()


        // Fügen Sie weitere Tests für andere Methoden hinzu

    }

    /**
     *  The function [rotateTileTest] the function rotate
     */
    @Test
    fun rotateTileTest() {
        val expectedTile = testTile
        val expectedTileRightRotated = testTile
        expectedTile.edges.add(0,expectedTile.edges.removeAt(expectedTile.edges.size-1))
        playerTurnService.rotateTileRight(testTile)
        assertEquals(expectedTileRightRotated,testTile)
        playerTurnService.rotateTileLeft(testTile)
        assertEquals(expectedTile,testTile)
    }
}
