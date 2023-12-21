package service.network

import edu.udo.cs.sopra.ntf.GameMode
import edu.udo.cs.sopra.ntf.Player
import edu.udo.cs.sopra.ntf.PlayerColor
import edu.udo.cs.sopra.ntf.TileType
import entity.Edge
import entity.Tile
import entity.Token
import entity.TokenColor
import service.RootService

/**
 * The class [NetworkMappingService] is for the translating
 * form the entity layer classes to the data classes for the
 * network
 *
 * @property rootService The rootService with all connection
 * to other Services
 */
class NetworkMappingService(private val rootService: RootService) {

    /**
     * The function [toGameMode] find the right mode for the GameInitMessage
     *
     * @return Returns the Gamemode which the players playing
     */
    fun toGameMode(): GameMode {
        val game = rootService.currentGame
        checkNotNull(game) { "game should not be null right after starting it." }
        val gateTokens = game.gameBoard.gateTokens
        val players = game.players
        var sharedGateWays = false
        for (i in gateTokens.indices step 2) {
            if (gateTokens[i] != gateTokens[i + 1]) {
                sharedGateWays = true
            }
        }
        var gameMode = GameMode.TWO_NOT_SHARED_GATEWAYS
        if (players.size == 3) {
            gameMode = if (sharedGateWays) GameMode.THREE_SHARED_GATEWAYS
            else GameMode.THREE_NOT_SHARED_GATEWAYS
        }
        if (players.size == 4) {
            gameMode = GameMode.FOUR_SHARED_GATEWAYS
        }
        return gameMode
    }

    /**
     *  the function [toTileTypeList] is make the entity route list
     *  to the tile type list for the network tile list.
     *
     *  @return a list of all routeTiles
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
            when (routeTile.paths) {
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
     *  to the network playerList
     *
     *  @return The List of the network player model
     */
    fun toNetworkPlayer(): List<Player> {
        val game = rootService.currentGame
        checkNotNull(game) { "game should not be null right after starting it." }
        val networkPlayers = mutableListOf<Player>()
        val players = game.players
        for (player in players) {
            val playerColor = when (player.color) {
                TokenColor.BLUE -> {
                    PlayerColor.BLUE
                }

                TokenColor.RED -> {
                    PlayerColor.RED
                }

                TokenColor.PURPLE -> {
                    PlayerColor.PURPLE
                }

                TokenColor.WHITE -> {
                    PlayerColor.WHITE
                }
            }
            networkPlayers.add(Player(player.name, playerColor))
        }
        return networkPlayers.toList()
    }

    /**
     * The function [toRouteTiles] is making the tileList to
     * the routeTileList of the entity class
     *
     * @return Returns a List of all route tile from the entity model.
     */
    fun toRouteTiles(tileList: List<TileType>): MutableList<Tile> {
        val routeTiles = mutableListOf<Tile>()
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
        for (tileType in tileList) {
            when (tileType) {
                TileType.TYPE_0 -> {
                    routeTiles.add(Tile(tile0, mutableMapOf()))
                }

                TileType.TYPE_1 -> {
                    routeTiles.add(Tile(tile1, mutableMapOf()))
                }

                TileType.TYPE_2 -> {
                    routeTiles.add(Tile(tile2, mutableMapOf()))
                }

                TileType.TYPE_3 -> {
                    routeTiles.add(Tile(tile3, mutableMapOf()))
                }

                TileType.TYPE_4 -> {
                    routeTiles.add(Tile(tile4, mutableMapOf()))
                }
            }
        }
        return routeTiles
    }

    /**
     *  The function [toEntityPlayer] make the Network player to entity Player
     *
     *  @param players players are all Players which participate the online game
     */
    fun toEntityPlayer(players: List<Player>): MutableList<entity.Player> {
        val entityPlayers = mutableListOf<entity.Player>()
        for (i in players.indices) {
            var tokenColor = TokenColor.WHITE
            when (players[i].color) {
                PlayerColor.BLUE -> {
                    tokenColor = TokenColor.BLUE
                }

                PlayerColor.RED -> {
                    tokenColor = TokenColor.RED
                }

                PlayerColor.PURPLE -> {
                    tokenColor = TokenColor.PURPLE
                }

                else -> {}
            }
            entityPlayers.add(entity.Player(name = players[i].name, color = tokenColor))
        }
        return entityPlayers
    }

    /**
     *  The function []
     * @param players The players which are participate the game
     * @param notSharedGates  The [notSharedGates] is two decide which you have
     *
     * @return A mutable list of token as gateTokens
     */
    fun toGateTokens(players: List<entity.Player>, gameMode: GameMode): MutableList<Token> {
        var notSharedGates = false
        when (gameMode) {
            GameMode.TWO_NOT_SHARED_GATEWAYS -> {
                notSharedGates = true
            }

            GameMode.THREE_NOT_SHARED_GATEWAYS -> {
                notSharedGates = true
            }

            else -> {}
        }
        return createGateTokens(players, notSharedGates)
    }

    /**
     * The private function [createGateTokens]
     * create from the upcoming information of the network
     * to make the gate tokens
     *
     * @param players The players which are participate the game
     * @param notSharedGates  The [notSharedGates] is two decide which mode the gates are
     * initialize
     *
     * @return A mutable list of token as gateTokens
     */
    private fun createGateTokens(players: List<entity.Player>, notSharedGates: Boolean): MutableList<Token> {
        val gateTokens = mutableListOf<Token>()
        val playerSize = players.size
        if (notSharedGates) {
            for (i in 0 until 6) {
                gateTokens.add(Token(players[i % playerSize].color))
                gateTokens.add(Token(players[i % playerSize].color))
            }
        } else {
            if (playerSize == 4) {
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[3].color))
                gateTokens.add(Token(players[3].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[3].color))
            }
            if (playerSize == 3) {
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[2].color))
                gateTokens.add(Token(players[0].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[1].color))
                gateTokens.add(Token(players[2].color))
            }
        }
        return gateTokens
    }
}