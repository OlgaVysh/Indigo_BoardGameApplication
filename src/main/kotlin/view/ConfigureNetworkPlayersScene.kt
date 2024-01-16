package view

import entity.Player
import entity.TokenColor
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
    val grid = GridPane<NetworkPlayersView>(960, 484, 1, 1, 10, true)

    // Number of games minus one to get the correct index
    //private val size = games.size - 1

    // Button for adding a new player
   val addButton = Button(188, 806, 528, 207, "Add new player", 40).apply {
        onMouseClicked = { indigoApp.showMenuScene(indigoApp.newPlayerScene) }
        isDisabled = grid.rows ==4
    }

    // Button for starting a new game
    val startButton = Button(1217, 806, 528, 207, "Start new game", 40).apply {
        isDisabled = grid.rows<2
        onMouseClicked = {
            //indigoApp.showGameScene(indigoApp.gameScene)
            val players = mutableListOf<Player>()
            for(player in indigoApp.players){
                if(player!=null){
                    players.add(player)
                }
            }
            indigoApp.notSharedGates = false
            if (players.size == 4) indigoApp.notSharedGates = true
            if (players.size == 3) {
                indigoApp.showMenuScene(indigoApp.gatesScene)
            } else {
                if (indigoApp.aiGame) {
                    indigoApp.hideMenuScene()
                    indigoApp.showMenuScene(indigoApp.aiMenuScene)
                } else {
                    val notSharedGates = indigoApp.notSharedGates
                    val isRandom = indigoApp.isRandom
                    indigoApp.rootService.networkService.startNewHostedGame(players, notSharedGates, isRandom)
                    players.clear()
                }
            }
        }
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
            indigoApp.players.add(
                Player(name = hostName, color = TokenColor.BLUE)
            )
            grid[0, 0] = NetworkPlayersView().apply {
                label.text = "Player " + grid.rows + ": " + hostName
                this.button.onMouseClicked = {
                    indigoApp.showMenuScene(indigoApp.configurePlayerXScene)
                    indigoApp.configurePlayerXScene.playerName = hostName
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
            indigoApp.players.add(
                Player(name = newPlayerName, color = TokenColor.BLUE)
            )
            val newNetworkPlayer = NetworkPlayersView(0, 0).apply {
                label.text = "Player " + grid.rows + ": " + newPlayerName
                this.button.onMouseClicked = {
                    indigoApp.showMenuScene(indigoApp.configurePlayerXScene)
                    indigoApp.configurePlayerXScene.playerName = newPlayerName
                }
                //label.posY = (151 * (currentRows - 1)).toDouble()
                //button.posY = (151 * (currentRows - 1)).toDouble()
            }
            grid[0, currentRows] = newNetworkPlayer
            startButton.isDisabled = grid.rows < 2
            addButton.isDisabled = grid.rows==4
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
        val removePlayer = indigoApp.players.find { it?.name == playerLeftName }
        indigoApp.players.remove(removePlayer)
        grid.removeEmptyRows()/*for (i in 0 until grid.rows) {
         val networkPlayer = grid.get(0, i) ?: continue
        networkPlayer.apply {
            posY = (151 * i).toDouble()
            label.posY = (151 * i).toDouble()
            button.posY = (151 * i).toDouble()
        }
        }*/
        startButton.isDisabled = grid.rows <2
        addButton.isDisabled = grid.rows==4
    }

    override fun refreshAfterStartGame() {
        val networkService = indigoApp.rootService.networkService
        val connectionState = networkService.connectionState
        if (connectionState != ConnectionState.DISCONNECTED) {
            indigoApp.showGameScene(indigoApp.gameScene)
        }
    }
}
