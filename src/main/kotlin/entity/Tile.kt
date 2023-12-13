package entity
class Tile(val paths: List<Pair<Edge,Edge>>,val gemEndPosition: Map<Int, Gem>) {
    val edges: List<Edge> = listOf(Edge.ZERO, Edge.ONE, Edge.TWO, Edge.THREE, Edge.FOUR, Edge.FIVE)
}