package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the menu scene for network game options.
 *
 * @param width The width of the scene.
 * @param height The height of the scene.
 * @param background The background image for the scene.
 */
class NetworkMenuScene : MenuScene(1920, 1080, background = ImageVisual("SevenGems2Background.png")) {
    // Buttons for network game options
    private val hostButton = Button(266, 642, 528, 207, "Host game", 48)
    private val joinButton = Button(1100, 642, 528, 207, "Join game", 48)

    // Labels for providing instructions
    private val networkLabel1 = Label(381, 370, 1111, 85, "Please, choose one of the following", 60)
    private val networkLabel2 = Label(381, 469, 1111, 85, "options :", 60)

    /**
     * Initializes the NetworkMenuScene with default values and sets up UI components.
     */
    init {
        // Set the initial opacity of the scene
        opacity = 1.0
        // Add components to the scene
        addComponents(networkLabel1, networkLabel2, hostButton, joinButton)
    }
}