import entity.Tile
/**
 * class modelling the GameBoard consisting of Tiles and Tokens
 * @property gameBoardTiles represents tiles on the board
 * @property gateTokens represents Tokens
 *
 */
class GameBoard {
    val gameBoardTiles = mutableMapOf<Coordinate, Tile>()
    val gateTokens = listOf<Token>()
}