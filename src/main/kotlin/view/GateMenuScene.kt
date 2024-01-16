package view

import entity.Player
import service.network.ConnectionState
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the menu scene for selecting gate options.
 *
 * @param width The width of the scene.
 * @param height The height of the scene.
 * @param background The background image for the scene.
 */

class GateMenuScene(val indigoApp: IndigoApplication) :
    MenuScene(1920, 1080, background = ImageVisual("SevenGems2Background.png")), Refreshable {
    // Buttons for selecting game modes
    private val sharedButton = Button(266, 642, 528, 207, "SharedGates", 48).apply {
        onMouseClicked = {
            val isRandom = indigoApp.isRandom
            val players = mutableListOf<Player>()
            for(player in indigoApp.players){
                if(player!=null){
                    players.add(player)
                }
            }
            val connectionState = indigoApp.rootService.networkService.connectionState
            if (connectionState == ConnectionState.DISCONNECTED) {
                indigoApp.rootService.gameService.startGame(players, false, isRandom)
            } else {
                indigoApp.rootService.networkService.startNewHostedGame(players, false, isRandom)
            }
            players.clear()
            indigoApp.hideMenuScene()
            indigoApp.showGameScene(indigoApp.gameScene)
        }
    }
    private val separatedButton = Button(1100, 642, 528, 207, "SeperatedGates", 48).apply {
        onMouseClicked = {
            val isRandom = indigoApp.isRandom
            val players = mutableListOf<Player>()
            for(player in indigoApp.players){
                if(player!=null){
                    players.add(player)
                }
            }
            val connectionState = indigoApp.rootService.networkService.connectionState
            if (connectionState == ConnectionState.DISCONNECTED) {
                indigoApp.rootService.gameService.startGame(players, true, isRandom)
            } else {
                indigoApp.rootService.networkService.startNewHostedGame(players, true, isRandom)
            }
            players.clear()
            indigoApp.hideMenuScene()
            indigoApp.showGameScene(indigoApp.gameScene)
        }
    }

    // Labels for providing instructions
    private val gatesLabel1 = Label(381, 370, 1111, 85, "Please, choose one of the following", 60)
    private val gatesLabel2 = Label(381, 469, 1111, 85, "options :", 60)

    init {        // Set the initial opacity of the scene

        opacity = 1.0
        // Add components to the scene
        addComponents(gatesLabel1, gatesLabel2, sharedButton, separatedButton)
    }
}