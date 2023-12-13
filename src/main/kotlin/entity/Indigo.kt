package entity

/**
 * class modelling the game state of an Indigo game, acts like an element in a doubly linked list
 *
 * @constructor creates an instance of Indigo with the given parameters
 *
 * @param players [List] of [Player] entities involved in the game
 * @param playerIndex [Int] serving as an indicator of whose turn it is, defaults to 0
 * @param isRandom [Boolean] which, when true overrides the [currentPlayerIndex] with a random value on init,
 * defaults to false
 * @param gameBoard current state of the [GameBoard]
 * @param allTiles [List] of treasure and route tiles
 * @param gems [MutableList] of [Gem]s currently still in play
 * @param tokens [MutableList] of [Token]s in the game
 *
 * @property currentPlayerIndex used to determine which [Player] is up next
 * @property middleTile representing the [MiddleTile] in the center of the board
 * @property treasureTiles [List] of Treasure [Tile]s on the board
 * @property routeTiles [MutableList] of Route [Tile]s currently still in play
 * @property previousGameState saves the previous state of the game for undo action, initially null
 * @property nextGameState saves the next state of the game for redo action, initially null
 *
 */
class Indigo(
    val players: List<Player>,
    playerIndex:Int = 0,
    isRandom: Boolean = false,
    val gameBoard: GameBoard,
    val allTiles: List<Tile>,
    var gems: MutableList<Gem>,
    var tokens: MutableList<Token>) {

    var currentPlayerIndex = 0
    val middleTile = MiddleTile()
    val treasureTiles: List<Tile> = allTiles.take(6)
    var routeTiles: MutableList<Tile> = allTiles.drop(6).toMutableList()
    var previousGameState: Indigo? = null
    var nextGameState: Indigo? = null
    init {
        currentPlayerIndex = if (isRandom) (0..players.size).random()
        else playerIndex
    }
}
