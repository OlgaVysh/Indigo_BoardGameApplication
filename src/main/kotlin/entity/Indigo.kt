package entity

/**
 * class modelling the game state of an Indigo game, acts like an element in a doubly linked list
 *
 * @constructor creates an instance of Indigo with the given parameters
 *
 * @param settings [players], initial [] and isRandom [Boolean] packaged into a wrapping class
 * @param gameBoard current state of the [GameBoard]
 * @param allTiles [List] of treasure and route tiles
 * @param gems [MutableList] of [Gem]s currently still in play
 * @param tokens [MutableList] of [Token]s in the game
 *
 * @property players [List] of [Player] entities involved in the game
 * @property currentPlayerIndex used to determine which [Player] is up next
 * @property middleTile representing the [MiddleTile] in the center of the board
 * @property treasureTiles [List] of Treasure [Tile]s on the board
 * @property routeTiles [MutableList] of Route [Tile]s currently still in play
 * @property previousGameState saves the previous state of the game for undo action, initially null
 * @property nextGameState saves the next state of the game for redo action, initially null
 *
 */

class Indigo(
    val settings: GameSettings,
    val gameBoard: GameBoard,
    val allTiles: List<Tile>,
    var gems: MutableList<Gem>,
    var tokens: MutableList<Token>
) {
    val players = settings.players
    var currentPlayerIndex = 0
    val middleTile = MiddleTile()
    val treasureTiles: List<Tile> = allTiles.take(6)
    var routeTiles: MutableList<Tile> = allTiles.drop(6).toMutableList()
    var previousGameState: Indigo? = null
    var nextGameState: Indigo? = null

    init {
        currentPlayerIndex = if (settings.isRandom) (0 until settings.players.size).random()
        else settings.playerIndex
    }
}
