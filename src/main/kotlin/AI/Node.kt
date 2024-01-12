package AI
import entity.Coordinate
import entity.Indigo
import service.GameService
import service.RootService

/**
 * Represents a node in the Monte Carlo Tree Search algorithm for game decision-making.
 *
 * @property rootService The RootService instance containing game-related information.
 * @property parent The parent node in the search tree (null if the node is the root).
 * @property move The move that leads from the parent node to this node.
 */


data class Node(val rootService: RootService, val parent: Node?, val move: Move) {
    val children: MutableList<Node> = mutableListOf()

    /**the state property is initialized based on whether the node is the root node or has a parent.
     * If it has a parent, the game state is updated by the move made from the parent node.
     * If it is the root node, the game state is initialized based on the current game obtained from the rootService.
     */

    val state: Indigo =
        if (parent != null) AiActionService.doMove(parent.state, move) else
            Indigo (rootService.currentGame!!.settings,
                rootService.currentGame!!.gameBoard,
                rootService.currentGame!!.allTiles,
                rootService.currentGame!!.gems,
                rootService.currentGame!!.tokens
            )

    var winCount = 0.0
    var visitCount = 0.0

    // Initialize current player index if the node is the root node
    init {
        if (parent == null) state.currentPlayerIndex = rootService.currentGame!!.currentPlayerIndex
    }

    /**
     * Returns a list of possible moves in this node's state.
     **
     * @return List of Move objects representing the possible moves in this node's state
     */
    fun getPossibleMoves(): MutableList<Move> {
        val availableMoves:MutableList<Move> = mutableListOf()

        // If the current player has no hand tile, return an empty list of moves
        val playerTile= state.players[state.currentPlayerIndex].handTile ?: return availableMoves

        // Iterate over the game board and find available moves
        for (row in -4..4) {
            for (col in -4..4) {
                val coordinate = Coordinate(row, col)
                // Check if placing the tile at the coordinate is a valid move
                if (GameService.checkPlacement(coordinate, playerTile)) {
                    availableMoves.add(Move(row,col))
                }
            }
        }
        return availableMoves
    }

}
