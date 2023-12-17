package service
import entity.*


class GameService(val rootService: RootService){
    fun startGame(){} //players: List<Player> :Indigo
    fun restartGame(){}//players: List<Player> :Indigo
    fun endGame(){}
    fun checkPlacement():Boolean{

        return true
    } //:Boolean
    fun checkCollision(){}//:Unit
    fun saveGame(){}
    fun loadGame(){}
    fun assignGems(){}//gem:Gem, player: Player
    fun changePlayer(){}//Unit
    fun moveGems(){}//(gem: Gem) :Unit
    fun addPoints(){}//(player: Player, amount: Int) : Unit
    fun distributeNewTile(){}//:Unit
    fun initializeTiles(){}//:Unit
    fun initializeGems(){}//:Unit
    fun removeGems(){}//(gems:List<Gem>):Unit
}