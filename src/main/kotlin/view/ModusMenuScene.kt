package view

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the menu scene for selecting game modes.
 * @param indigoApp represents an object of [IndigoApplication]
 */

class ModusMenuScene (indigoApp : IndigoApplication) : MenuScene(1920, 1080, background = ImageVisual("SevenGems2Background.png")) {
    // Buttons for selecting game modes
    private val hotSeatButton = Button(266, 642, 528, 207, "HotSeat", 48)
    private val networkButton = Button(1100, 642, 528, 207, "Network", 48)

    // Labels for providing instructions
    private val modusLabel1 = Label(639, 388, 642, 85, "Please, choose your", 60)
    private val modusLabel2 = Label(639, 455, 642, 85, "game mode:", 60)

    /**
     * Initializes the ModusMenuScene with default values and sets up UI components.
     */
    init {
        // Set the initial opacity of the scene
        opacity = 1.0
        // Add components to the scene
        addComponents(modusLabel1, modusLabel2, networkButton, hotSeatButton)
    }
}