package view

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

class GateMenuScene : MenuScene(1920, 1080, background = ImageVisual("SevenGems2Background.png")) {
    // Buttons for selecting game modes
    private val sharedButton = Button(266, 642, 528, 207, "HotSeat", 48)
    private val separatedButton = Button(1100, 642, 528, 207, "Network", 48)

    // Labels for providing instructions
    private val gatesLabel1 = Label(381, 370, 1111, 85, "Please, choose one of the following", 60)
    private val gatesLabel2 = Label(381, 469, 1111, 85, "options :", 60)

    init {        // Set the initial opacity of the scene

        opacity = 1.0
        // Add components to the scene
        addComponents(gatesLabel1, gatesLabel2, sharedButton, separatedButton)
    }
}