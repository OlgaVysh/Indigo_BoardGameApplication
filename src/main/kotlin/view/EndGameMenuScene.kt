package view

import entity.Player
import service.RootService
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the end game menu scene.
 *
 * This scene is displayed when a game reaches its conclusion. It includes a label indicating the game is over,
 * and buttons for exiting the game or starting a new game.
 *
 * The layout and design of these components are defined in this class.
 *
 * @property rootService An instance of RootService to access game-related functionalities.
 */
class EndGameMenuScene(private val indigoApp: IndigoApplication) : MenuScene(1920, 1080), Refreshable {
    private val game = indigoApp.rootService.currentGame

    // Label to display the "Game Over" Header.
    private val gameOverLabel = Label(650, 48, 620, 155, "Game Over", 120)

    private val betweenGemsLabel = Label(502, 307, 961, 77, "Player 1 has won with XY Points", 64)
    val player1 = Label(719, 417, 771, 59, "Player 1 has XY Points.", 48).apply {
        isVisible = false
        alignment = Alignment.CENTER_LEFT
    }
    val player2 = Label(
        width = 771, height = 59,
        posX = 719, posY = 503,
        fontSize = 48,
        text = "Player 2 has XY Points."
    ).apply { alignment = Alignment.CENTER_LEFT }
    private val player3 = Label(
        width = 771, height = 59,
        posX = 719, posY = 589,
        text = "Player 3 has XY Points.",
        fontSize = 48
    ).apply {
        isVisible = false
        alignment = Alignment.CENTER_LEFT
    }
    private val player4 = Label(
        width = 771, height = 59,
        posX = 719, posY = 675,
        fontSize = 48,
        text = "Player 4 has XY Points."
    ).apply {
        isVisible = false
        alignment = Alignment.CENTER_LEFT
    }


    // Button for exiting and starting a new game.
    private val exitButton = Button(185, 780, 532, 207, "Exit", 48)
    private val newGameButton = Button(1180, 780, 532, 207, "Start new game", 48)

    // Setting the background and adding components to the scene.
    init {
        background = ImageVisual("FiveGemsBackGround.png")
        opacity = 1.0
        addComponents(
            betweenGemsLabel,
            player1,
            player2,
            player3,
            player4,
            gameOverLabel,
            exitButton,
            newGameButton
        )
    }

    override fun refreshAfterEndGame() {
        checkNotNull(game)
        val players = game.players
        val sortedPlayers =
            players.sortedWith(compareByDescending<Player> { it.score }.thenByDescending { it.gemCounter })
        betweenGemsLabel.text = players[0].name + " has " + players[0].score + "Points."
        player2.text = players[1].name + " has " + players[1].score + "Points."
        if (sortedPlayers[0].score == sortedPlayers[1].score
            && sortedPlayers[0].gemCounter == sortedPlayers[1].gemCounter
        ) {
            betweenGemsLabel.text = "It's a Tie between " + sortedPlayers[0].name + " and " +
                    sortedPlayers[1].name
            player1.isVisible = true
            player1.text = players[0].name + " has " + players[0].score + "Points and " +
                    players[0].gemCounter + " Gems."
            player1.posX = 605.0
            player2.text = players[1].name + " has " + players[1].score + "Points and " +
                    players[1].gemCounter + " Gems."
            player2.posX = 605.0
        }

        if (players.size >= 3) {

            player3.text = players[2].name + " has " + players[2].score + "Points."
            player3.isVisible = true
            if (sortedPlayers[0].score == sortedPlayers[2].score
                && sortedPlayers[0].gemCounter == sortedPlayers[2].gemCounter
            ) {
                betweenGemsLabel.text = "It's a Tie between " + sortedPlayers[0].name + ", " +
                        sortedPlayers[1].name + " and " + sortedPlayers[2].name
                player3.isVisible = true
                player3.text = players[2].name + " has " + players[2].score + "Points and " +
                        players[2].gemCounter + " Gems."
                player3.posX = 605.0
            }
        }
        if (players.size == 4) {
            player4.text = players[3].name + " has " + players[3].score + "Points."
            player4.isVisible = true
            if (sortedPlayers[0].score == sortedPlayers[3].score
                && sortedPlayers[0].gemCounter == sortedPlayers[3].gemCounter
            ) {
                betweenGemsLabel.text = "It's a Tie between " + sortedPlayers[0].name + ", " +
                        sortedPlayers[1].name + ", " +
                        sortedPlayers[2].name + " and " + sortedPlayers[3].name
                player4.isVisible = true
                player4.text = players[3].name + " has " + players[3].score + "Points and " +
                        players[3].gemCounter + " Gems."
                player4.posX = 605.0
            }
        }
    }
}