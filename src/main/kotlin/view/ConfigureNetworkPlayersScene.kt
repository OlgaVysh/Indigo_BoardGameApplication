package view

import view.components.*

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

/**
 * Scene for configuring network players before starting a new game.
 *
 * @param games List of strings representing available games for configuration.
 */
class ConfigureNetworkPlayersScene(games: List<String>) :
    BoardGameScene(1920, 1080, background = ImageVisual("PlainBackground_FCE6BD.png")) {    // Title label for the scene

    private val label = Label(453, 21, 1050, 155, "Configure Players", 120)

    // Grid for displaying NetworkPlayersView for each game
    private val grid = GridPane<NetworkPlayersView>(960, 484, 1, games.size, 10, true)

    // Number of games minus one to get the correct index
    private val size = games.size - 1

    // Button for adding a new player
    private val addButton = Button(188, 806, 528, 207, "Add new player", 40)

    // Button for starting a new game
    private val startButton = Button(1217, 806, 528, 207, "Start new game", 40)

    /**
     * Initializes the ConfigureNetworkPlayersScene.
     */

    init {
        // Populate the grid with NetworkPlayersView instances

        for (i in 0..size) {
            grid[0, i] = NetworkPlayersView()
        }
        // Add components to the scene
        addComponents(label, grid, addButton, startButton)
    }
}
