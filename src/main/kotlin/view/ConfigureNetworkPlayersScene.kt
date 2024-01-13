package view

import service.RootService
import service.network.ConnectionState
import view.components.*

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ImageVisual

/**
 * Scene for configuring network players before starting a new game.
 *
 * //@param games List of strings representing available games for configuration.
 */
class ConfigureNetworkPlayersScene(val indigoApp: IndigoApplication/*, games: List<String>*/) : BoardGameScene(
    1920, 1080, background = ImageVisual("PlainBackground_FCE6BD.png")
), Refreshable {    // Title label for the scene

    private val label = Label(453, 21, 1050, 155, "Configure Players", 120)

    // Grid for displaying NetworkPlayersView for each game
    private val grid = GridPane<NetworkPlayersView>(960, 484, 1, 1, 10, true)

    // Number of games minus one to get the correct index
    //private val size = games.size - 1

    // Button for adding a new player
    private val addButton = Button(188, 806, 528, 207, "Add new player", 40)

    // Button for starting a new game
    private val startButton = Button(1217, 806, 528, 207, "Start new game", 40).apply {
        onMouseClicked = { indigoApp.showGameScene(indigoApp.gameScene) }
    }

    /**
     * Initializes the ConfigureNetworkPlayersScene.
     */

    init {
        // Populate the grid with NetworkPlayersView instances

        //  for (i in 0..size) {
        grid[0, 0] = NetworkPlayersView().apply {
            this.button.onMouseClicked = { indigoApp.showMenuScene(indigoApp.configurePlayerXScene) }
        }
        //}
        // Add components to the scene
        addComponents(label, grid, addButton, startButton)
    }

    /**
     *  The function set the first row as the Host Player
     *
     *  @param sessionID give an sessionID  if is not Error from the server
     */
    override fun refreshAfterOnCreateGameResponse(sessionID: String?) {
        val networkService = indigoApp.rootService.networkService
        val connectionState = networkService.connectionState
        if (connectionState == ConnectionState.WAITING_FOR_GUEST) {
            val hostName = networkService.client!!.playerName
            grid[0, 0] = NetworkPlayersView().apply {
                label.text = "Player " + grid.rows + ": " + hostName
                this.button.onMouseClicked = {
                    indigoApp.showMenuScene(indigoApp.configurePlayerXScene)
                }
            }
        }
    }

    /**
     *  The Methode [refreshAfterPlayerJoined] added in the grid the new joined Player
     *
     *  @param newPlayerName is a String which contains the name of the new joined Player
     */
    override fun refreshAfterPlayerJoined(newPlayerName: String) {
        val currentRows = grid.rows
        if (currentRows < 4) {
            grid.addRows(currentRows)
            val newNetworkPlayer = NetworkPlayersView(0, 0).apply {
                label.text = "Player " + grid.rows + ": " + newPlayerName
                this.button.onMouseClicked = {
                    indigoApp.showMenuScene(indigoApp.configurePlayerXScene)
                }
                //label.posY = (151 * (currentRows - 1)).toDouble()
                //button.posY = (151 * (currentRows - 1)).toDouble()
            }
            grid[0, currentRows] = newNetworkPlayer
        } else {
            val rootService = RootService()
            val networkClient = rootService.networkService.client
            checkNotNull(networkClient)
            networkClient.otherPlayers.remove(newPlayerName)
        }
    }

    /**
     *  The Methode [refreshAfterPlayerJoined] remove the new left Player in the grid
     *
     *  @param playerLeftName is a String which contains the name of the left Player
     */
    override fun refreshAfterPlayerLeft(playerLeftName: String) {
        for (i in 0 until grid.rows) {
            val networkPlayer = grid[0, i] ?: continue
            if (networkPlayer.label.name.contains(playerLeftName)) {
                grid.removeRow(i)
                break
            }
        }
        grid.removeEmptyRows()/*for (i in 0 until grid.rows) {
         val networkPlayer = grid.get(0, i) ?: continue
        networkPlayer.apply {
            posY = (151 * i).toDouble()
            label.posY = (151 * i).toDouble()
            button.posY = (151 * i).toDouble()
        }
        }*/
    }
}
