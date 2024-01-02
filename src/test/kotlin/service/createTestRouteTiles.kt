package service
import entity.Edge
import entity.Tile

/**
 * the function create a  test list of route tiles
 *
 *  @return A mutable List of route tiles
 */
fun createTestRouteTile() : MutableList<Tile>{
    val tile0 = listOf(
        Pair(Edge.ZERO, Edge.TWO),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.THREE, Edge.FIVE)
    )
    val tile1 = listOf(
        Pair(Edge.TWO, Edge.FIVE),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.ZERO, Edge.THREE)
    )
    val tile2 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.TWO, Edge.THREE)
    )
    val tile3 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.THREE),
        Pair(Edge.TWO, Edge.FOUR)
    )
    val tile4 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.TWO),
        Pair(Edge.THREE, Edge.FOUR)
    )
    val routeTiles = mutableListOf<Tile>()
    repeat(14){
        routeTiles.add(Tile(tile0, mutableMapOf()))
    }
    repeat(6){
        routeTiles.add(Tile(tile1, mutableMapOf()))
    }
    repeat(14){
        routeTiles.add(Tile(tile2, mutableMapOf()))
    }
    repeat(14){
        routeTiles.add(Tile(tile3, mutableMapOf()))
    }
    repeat(6){
        routeTiles.add(Tile(tile4, mutableMapOf()))
    }
    return routeTiles
}