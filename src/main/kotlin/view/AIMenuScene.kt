package view

import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import view.components.Button
import view.components.Label

/**
 * Represents the menu scene for an AI game.
 *
 * This scene includes various UI components like labels, a combo box for selecting AI speed, and a button to start the game.
 * The layout and visual elements are defined within this class.
 *
 */
class AIMenuScene (indigoApp : IndigoApplication) : MenuScene (1920, 1080),Refreshable {


    // Label to display the header.
    private val aiGameLabel = Label (397, 71, 1126,155,"This is an AI Game!",120)

    // Labels for instructions regarding the simulation speed.
    private val speed1Label = Label (397, 324,1126,77,"Please, choose the simulation speed :",64)
    private val speed2Label = Label (397, 416,1192,58,"(Default speed is middle)",48)
    private val speed3Label = Label (230, 508,1192,58,"Set speed to :",48)

    // ComboBox to allow the user to select the AI speed.
    private val aiSpeed = ComboBox<String>(1015, 495, 300, 69, prompt = "Select ai speed")

    // Button to start the game.
    private val startButton = Button(730, 805,532,207,"Start new game",48).
    apply { onMouseClicked ={indigoApp.showGameScene(indigoApp.gameScene)
    indigoApp.hideMenuScene()}}

    // Setting the background and adding all components to the scene.
    init{
        background =  ImageVisual("ThreeGemsBackground.png")
        opacity = 1.0
        addComponents(
            aiGameLabel,
            speed1Label,
            speed2Label,
            speed3Label,
            startButton,
            aiSpeed)
        aiSpeed.items = mutableListOf("low","middle","high")
    }
}