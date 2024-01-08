package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Menu scene displayed when a game has been successfully saved.
 * Extends [MenuScene] to inherit basic menu scene functionalities.
 */
class GameSavedMenuScene : MenuScene(1920, 1080, background = ImageVisual("SevenGems2Background.png")) {
    // Button for exiting to the main menu or closing the application
    private val hotSeatButton = Button(266, 642, 528, 207, "Exit", 48)

    // Button for starting a new game after saving
    private val networkButton = Button(1100, 642, 528, 207, "Start new game", 48)

    // Label indicating that the game has been saved
    private val label1 = Label(587, 271, 736, 155, "Game saved!", 120)

    /**
     * Initializes the GameSavedMenuScene.
     */

    init {        // Set the initial opacity

        opacity = 1.0
        // Add components to the scene
        addComponents(label1, networkButton, hotSeatButton)
    }
}