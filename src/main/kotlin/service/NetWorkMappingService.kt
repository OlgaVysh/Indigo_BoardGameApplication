package service

import edu.udo.cs.sopra.ntf.GameMode
import edu.udo.cs.sopra.ntf.Player
import edu.udo.cs.sopra.ntf.PlayerColor
import edu.udo.cs.sopra.ntf.TileType
import entity.Edge
import entity.TokenColor

/**
 * The class [NetworkMappingService] is for the translating
 * form the entity layer classes to the data classes for the
 * network
 *
 * @property rootService The rootService with all connection
 * to other Services
 */
class NetworkMappingService(private val rootService: RootService) {
    fun toGameModeMapping(): GameMode {
        val game = rootService.currentGame
        checkNotNull(game) { "game should not be null right after starting it." }
        val gateTokens = game.gameBoard.gateTokens
        val players = game.players
        var sharedGateWays = false
        for (i in 0 until gateTokens.size - 1 step 2) {
            if (gateTokens[i] == gateTokens[i + 1]) {
                sharedGateWays = true
            }
        }
        var gameMode = GameMode.TWO_NOT_SHARED_GATEWAYS
        if (players.size == 3) {
            gameMode = if (sharedGateWays) GameMode.THREE_SHARED_GATEWAYS
            else GameMode.THREE_NOT_SHARED_GATEWAYS
        }
        if (players.size == 4) gameMode = GameMode.FOUR_SHARED_GATEWAYS
        return gameMode
    }

    /**
     *  the function [toTileTypeList] is make the entity route list
     *  to the tile type list for the network tile list.
     */
    fun toTileTypeList(): List<TileType> {
        val game = rootService.currentGame
        checkNotNull(game) { "game should not be null right after starting it." }
        val tileList = mutableListOf<TileType>()
        val routeTiles = game.routeTiles
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
        for (routeTile in routeTiles) {
            when (routeTile.edges) {
                tile0 -> {
                    // Do something specific for tile0
                    tileList.add(TileType.TYPE_0)
                }

                tile1 -> {
                    // Do something specific for tile1
                    tileList.add(TileType.TYPE_1)
                }

                tile2 -> {
                    // Do something specific for tile2
                    tileList.add(TileType.TYPE_2)
                }

                tile3 -> {
                    // Do something specific for tile3
                    tileList.add(TileType.TYPE_3)
                }

                tile4 -> {
                    // Do something specific for tile4
                    tileList.add(TileType.TYPE_4)
                }
            }
        }
        return tileList
    }

    /**
     *  The funktion[toNetworkPlayer] make the entity player list
     *  to the
     */
    fun toNetworkPlayer() :List<Player>  {
        val game = rootService.currentGame
        checkNotNull(game) { "game should not be null right after starting it." }
        val networkPlayers = mutableListOf<Player>()
        val players = game.players
        for (player in players){
            val playerColor = when(player.color){
                TokenColor.BLUE -> {
                    PlayerColor.BLUE}
                TokenColor.RED -> {
                    PlayerColor.RED}
                TokenColor.PURPLE -> {
                    PlayerColor.PURPLE}
                TokenColor.WHITE -> {
                    PlayerColor.WHITE}
            }
            networkPlayers.add(Player(player.name,playerColor))
        }
        return networkPlayers.toList()
    }
}