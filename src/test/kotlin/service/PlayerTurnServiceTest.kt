package service
/*
import org.junit.jupiter.api.Test
import entity.*
import service.*
class PlayerTurnServiceTest {

        private lateinit var rootService: RootService
        private lateinit var gameService: GameService
        private lateinit var playerTurnService: PlayerTurnService


        fun setUp() {
            rootService = RootService()  // Stellen Sie sicher, dass Sie eine Instanz von RootService erstellen oder entsprechend initialisieren.
            gameService = GameService(rootService)
            playerTurnService = PlayerTurnService(rootService)
        }

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

        @Test
        fun testUndoRedo() {
            // Hier können Sie Ihre Testlogik für undo und redo implementieren
            // Zum Beispiel: Überprüfen Sie, ob das Undo und Redo wie erwartet funktioniert.

            // Beispiel:
            try {
                gameService.startGame()
                // Hier können Sie Aktionen ausführen, um das Spielzustand zu ändern

                // Führen Sie undo und redo durch
                playerTurnService.undo()
                playerTurnService.redo()

                // Hier können Sie Assertions platzieren, um sicherzustellen, dass die Operationen erfolgreich waren.
            } catch (e: Exception) {
                // Wenn Sie erwarten, dass eine Ausnahme geworfen wird, können Sie sie hier erfassen.
                // Beispiel: fail("Erwartete keine Ausnahme, aber es wurde eine geworfen.")
            }
        }

        // Fügen Sie weitere Tests für andere Methoden hinzu

    }

}

*/