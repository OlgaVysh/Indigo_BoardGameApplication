package AI
import entity.Coordinate
import service.AbstractRefreshingService

/**
 * The MCTS: Monte Carlo Tree Search algorithm to find the best player moves
 *
 * @param rootService the RootService instance that holds the game state information
 * @property aiActionService to get access to functions from the [AiActionService]
 * @property aiIndex the index of the player that the AI is playing as.
 */
class MCTS (private val rootService: service.RootService, private val aiIndex: Int): AbstractRefreshingService() {

    private val aiActionService = rootService.aiActionService

    /**
     * This method initiates the MCTS algorithm to find the next best move for the AI player.
     * It starts with a root node and iteratively performs selection, expansion, simulation, and backpropagation
     * until a stopping condition is met: "game is over" or " If there are no more possible moves to explore from the current node"
     *
     * @return A [Coordinate] object representing the best move for the AI player.
     */
    fun findNextMove() : Coordinate {
        val defaultMove = Coordinate( -1, -1)
        val root = Node(rootService, null, defaultMove)

        var terminateCondition = false
        while (true) {
            val node = selectPromisingNode(root)
            if (aiActionService.isGameOver(node.state) || terminateCondition) {
                backpropagation(node, true)
                return node.coordinate
            }
            terminateCondition = expandNode(node)
            val nodeToExplore = selectPromisingNode(node)
            val aiWon = simulateRandomPlayout(nodeToExplore)
            backpropagation(nodeToExplore, aiWon)
        }
    }


    /**
     * A simplified version of findNextMove that doesn't go through all the tree.
     * It performs a limited number of iterations to make a quick decision.
     *
     * @param maxIterations The maximum number of iterations to perform.
     * @return A [Coordinate] object representing the best move for the AI player.
     */
    fun findNextMoveLimited(maxIterations: Int): Coordinate {
        val defaultMove = Coordinate(-1, -1)
        val root = Node(rootService, null, defaultMove)

        repeat(maxIterations) {
            val node = selectPromisingNode(root)
            if (aiActionService.isGameOver(node.state)) {
                return node.coordinate
            }
            expandNode(node)
            val nodeToExplore = selectPromisingNode(node)
            simulateRandomPlayout(nodeToExplore)
            backpropagation(nodeToExplore, true)
        }

        return root.children.maxByOrNull { it.visitCount }?.coordinate ?: defaultMove
    }



    /**
     * selectPromisingNode - a function that selects the most promising node to be expanded
     * It traverses the tree to find the most promising node based on the UCT (Upper Confidence Bound applied to Trees) formula
     * @param node: the current node
     * @return Node: the most promising node
     */
    private fun selectPromisingNode(node: Node): Node {
        var current = node
        while (current.children.isNotEmpty()) {
            current = current.children.maxByOrNull {
                if (it.visitCount != 0.0) it.winCount / it.visitCount + Math.sqrt(2.0 * Math.log(node.visitCount) / it.visitCount)
                else Double.POSITIVE_INFINITY
            }!!
        }
        return current
    }

    /**
     * expandNode - a function that expands the given node by adding its possible moves as children
     * @param node: the node to be expanded
     * @return Boolean: returns true if there are no possible moves (i.e., the node is a terminal state)
     * false otherwise
     */

    private fun expandNode(node: Node): Boolean {
        node.getPossibleMoves().forEach {
            val child = Node(rootService, node, it)
            node.children.add(child)
        }
        node.children.shuffle()
        return node.children.isEmpty()
    }
    /**
     * simulateRandomPlayout - a function that simulates a random playout from the given node
     * @param node: the starting node for the simulation
     * @return Boolean: returns true if the AI won the simulation, false otherwise
     */
    private fun simulateRandomPlayout(node: Node): Boolean {
        var tempNode = node.copy()
        var stop = false
        while (!aiActionService.isGameOver(tempNode.state) && !stop) {
            stop = expandNode(tempNode)
            tempNode = selectPromisingNode(tempNode)
        }

        return tempNode.state.players[aiIndex].score >= tempNode.state.players.maxOf { it.score }
    }
    /**
     * backpropagation the result of the simulation up the tree to update the statistics
     * of each node.
     *
     * @param last Node at the end of the simulation
     * @param aiWon Boolean indicating if the AI won the simulation
     */
    private fun backpropagation(last: Node, aiWon: Boolean) {
        // It starts from the last node (end of the simulation) and moves up the tree until the root node is reached.
        var node: Node? = last
        while (node != null) {
            node.visitCount += 1
            if (aiWon) node.winCount += 1
            node = node.parent
        }
    }

}