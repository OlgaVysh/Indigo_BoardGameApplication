package view.components
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class PlayerView(name : String, turn :String = "", color:String ="", posX : Int = 0, posY : Int = 0) :
    GridPane<Label>(posX,posY,3,1, visual = ColorVisual(253,240,216))
{
    init {
        //name
        this[0,0] = Label(text = name, width = 200, height = 65, fontSize = 40)
        //color
        getGem(color)
        //turn
        var newTurn = ""
        if (turn!="null") newTurn = turn
        this[2,0] = Label(text = "turn : "+ newTurn, width = 400, height = 65,fontSize = 40)
    }

    private fun getGem(color:String)
    {
        if(color!="")
        {
            if(color=="red") this[1, 0] = Label(text = "").apply{visual = ImageVisual("tokenred.png")}
            if(color=="white") this[1, 0] = Label(text = "").apply{visual = ImageVisual("tokenwhite.png")}
            if(color=="blue") this[1, 0] = Label(text = "").apply{visual = ImageVisual("tokenblue.png")}
            if(color=="purple") this[1, 0] = Label(text = "").apply{visual = ImageVisual("tokenpurple.png")}
        }
        else this[1, 0] = Label(text = "color : ", width = 400, height = 65,fontSize = 40)
    }
}