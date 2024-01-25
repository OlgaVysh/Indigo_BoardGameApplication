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

data class Indigo(
    val settings: GameSettings,
    val gameBoard: GameBoard,
    val allTiles: List<Tile>,
    var gems: MutableList<Gem>,
    var tokens: MutableList<Token>
) {
    var players = settings.players
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

    /**
     *  the extension function [copyTo] is a function wich create a deep copy of Indigo
     *  with the necessary data
     *
     *  @return Returning a new [Indigo] which are independent of the current game
     */
    fun copyTo(): Indigo {
        val copiedGems = mutableListOf<Gem>()
        for (gem in gems) {
            copiedGems.add(gem)
        }
        val copiedGameBoardTiles = mutableMapOf<Coordinate,Tile>()
        for((key, value ) in this.gameBoard.gameBoardTiles ){
            val gemEndPosition = value.gemEndPosition.toMutableMap()
            val egdes = value.edges.toMutableList()
            val paths = value.paths.toMutableList()
            val copiedTile = Tile(paths,value.type,gemEndPosition).apply {
                this.edges.clear()
                this.edges.addAll(egdes)
            }
            copiedGameBoardTiles[key] = copiedTile
        }
        val copiedGateTokens = this.gameBoard.gateTokens.toList()
        val copiedGameBoard = GameBoard()
        copiedGameBoard.gameBoardTiles.clear()
        copiedGameBoard.gameBoardTiles.putAll(copiedGameBoardTiles)
        copiedGameBoard.gateTokens = copiedGateTokens
        val copiedPlayers = settings.players.map { originalPlayer ->
            when (originalPlayer) {
                is CPUPlayer -> {
                    CPUPlayer(
                        originalPlayer.name,
                        originalPlayer.age,
                        originalPlayer.color,
                        originalPlayer.difficulty,
                        originalPlayer.simulationSpeed
                    ).apply {
                        score = originalPlayer.score
                        collectedGems = originalPlayer.collectedGems.toMutableList()
                        // Copy the handTile
                        handTile = originalPlayer.handTile?.copy()
                        // Additional properties specific to CPUPlayer
                        // ...
                    }
                }

                else -> {
                    Player(
                        originalPlayer.name,
                        originalPlayer.age,
                        originalPlayer.color,
                        originalPlayer.isAI
                    ).apply {
                        score = originalPlayer.score
                        collectedGems = originalPlayer.collectedGems.toMutableList()
                        // Copy the handTile
                        handTile = originalPlayer.handTile?.copy()
                    }
                }
            }
        }.toList()
        val copiedSettings = GameSettings(copiedPlayers)
        val copiedIndigo = Indigo(
            copiedSettings,
            copiedGameBoard,
            this.allTiles,
            copiedGems,
            this.tokens
        )
        copiedIndigo.currentPlayerIndex = this.currentPlayerIndex
        copiedIndigo.nextGameState = this.nextGameState
        copiedIndigo.previousGameState = this.previousGameState
        copiedIndigo.middleTile.gemPosition.clear()
       for ((key,value ) in this.middleTile.gemPosition) {
            copiedIndigo.middleTile.gemPosition[key] =value
        }
        copiedIndigo.routeTiles = this.routeTiles.toMutableList()
        return copiedIndigo
    }
}
