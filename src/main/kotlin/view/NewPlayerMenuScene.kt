package view
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.ImageVisual
import service.RootService
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.Alignment
import view.components.Button
import view.components.Label
/**
 * Represents the new player menu scene.
 *
 * This scene shows a list of all actual players, their token and turn.
 * It is possible to add players if less than four players are actually in the player list
 * and start the game which leads to the game scene (?)
 *
 * The layout and design of these components are defined in this class.
 *
 * @property rootService An instance of RootService to access game-related functionalities.
 */
class NewPlayerMenuScene(private val rootService: RootService) : MenuScene(1920, 1080){
    private val titleLabel = Label(397, 80, 1058, 155, "Configure Players", 128)
    private val startGameButton = Button(1047,755,528,207, "Start game", 48)
    private val addNewPlayerButton = Button(340,755,528,207, "Add new player", 48)
    private val game = rootService.currentGame
    init {
        background = ImageVisual("ThreeGemsBackground.png")
        opacity = 1.0
        addComponents(titleLabel)
        addComponents(startGameButton)
        addComponents(addNewPlayerButton)
        val playerLabel = GridPane <Label>(posX = 400, posY = 300, layoutFromCenter = false, rows = 4, columns = 3, spacing = 105)
        val gemLabel1 = Label(text = "")
        val gemLabel2 = Label(text = "")
        val gemLabel3 = Label(text = "")
        val gemLabel4 = Label(text = "")
        //val gemLabelList = mutableListOf<Label>(gemLabel1, gemLabel2, gemLabel3, gemLabel4)
        //Einbauen sobald Service fertig
        //ggf. label.width individuell (max player names)
        /*
        for (i in 0 .. game!!.players.size){
           playerLabel[0,i] = Label(text = "Player $i: game.players[i].name", width = 600, fontSize = 48)
           gemLabelList[i].visual = game.players[i].color.toImg()
           playerLabel[1,i] = gemLabelList[i]
        }

         */

        //Testwerte
        playerLabel[0,0] = Label(text = "Player 1: "+"Olga", width = 600, fontSize = 48)
        playerLabel[0,0]!!.alignment = Alignment.TOP_LEFT
        gemLabel1.visual = ImageVisual("tokenblue.png")
        playerLabel[1,0] = gemLabel1
        playerLabel[2,0] = Label(text = "turn: "+"1st" ,width =450, fontSize = 48)
        playerLabel[2,0]!!.alignment = Alignment.TOP_LEFT

        playerLabel[0,1] = Label(text = "Player 2: "+"Semi", width = 600, fontSize = 48)
        playerLabel[0,1]!!.alignment = Alignment.TOP_LEFT
        gemLabel2.visual = ImageVisual("tokenred.png")
        playerLabel[1,1] = gemLabel2
        playerLabel[2,1] = Label(text = "turn: "+"2nd",width =450, fontSize = 48)
        playerLabel[2,1]!!.alignment = Alignment.TOP_LEFT

        //längster noch lesbarer Name ist 15 Chars lang
        playerLabel[0,2] = Label(text = "Player 3: "+"012345678901234", width = 600, fontSize = 48)
        playerLabel[0,2]!!.alignment = Alignment.TOP_LEFT
        gemLabel3.visual = ImageVisual("tokenwhite.png")
        playerLabel[1,2] = gemLabel3
        playerLabel[2,2] = Label(text = "turn: "+"3rd",width =450, fontSize = 48)
        playerLabel[2,2]!!.alignment = Alignment.TOP_LEFT

        playerLabel[0,3] = Label(text = "Player 4: "+"Maria", width = 600, fontSize = 48)
        playerLabel[0,3]!!.alignment = Alignment.TOP_LEFT
        gemLabel4.visual = ImageVisual("tokenpurple.png")
        playerLabel[1,3] = gemLabel4
        playerLabel[2,3] = Label(text = "turn: "+"4th",width =450, fontSize = 48)
        playerLabel[2,3]!!.alignment = Alignment.TOP_LEFT

        addComponents(playerLabel)

    }

}