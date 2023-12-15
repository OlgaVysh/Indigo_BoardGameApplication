package service

import edu.udo.cs.sopra.ntf.GameMode
import edu.udo.cs.sopra.ntf.TileType
import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

/**
 *  The class [NetworkMappingServiceTest] is testing
 *  all function of the [NetworkMappingService]
 */
class NetworkMappingServiceTest {
    private val players = listOf(
        Player(name = "John", color = TokenColor.PURPLE),
        Player(name = "Alice", color = TokenColor.BLUE),
        Player(name = "Bob", color = TokenColor.WHITE),
    )
    private val gameSetting = GameSettings(players)
    private val tile0 = listOf(
        Pair(Edge.ZERO, Edge.TWO),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.THREE, Edge.FIVE)
    )
    private val tile1 = listOf(
        Pair(Edge.TWO, Edge.FIVE),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.ZERO, Edge.THREE)
    )
    private val tile2 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.FOUR),
        Pair(Edge.TWO, Edge.THREE)
    )
    private val tile3 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.THREE),
        Pair(Edge.TWO, Edge.FOUR)
    )
    private val tile4 = listOf(
        Pair(Edge.ZERO, Edge.FIVE),
        Pair(Edge.ONE, Edge.TWO),
        Pair(Edge.THREE, Edge.FOUR)
    )
    private val placeTiles = mutableListOf(
        Tile(listOf(), mapOf()),
        Tile(listOf(), mapOf()),
        Tile(listOf(), mapOf()),
        Tile(listOf(), mapOf()),
        Tile(listOf(), mapOf()),
        Tile(listOf(), mapOf()),
    )
    private val routeTiles = mutableListOf(
        Tile(tile0, mapOf()),
        Tile(tile1, mapOf()),
        Tile(tile2, mapOf()),
        Tile(tile3, mapOf()),
        Tile(tile4, mapOf())
    )
    private val allTiles = placeTiles.addAll(routeTiles)
    private val tokens = mutableListOf(
        Token(TokenColor.PURPLE),
        Token(TokenColor.PURPLE),
        Token(TokenColor.BLUE),
        Token(TokenColor.BLUE),
        Token(TokenColor.WHITE),
        Token(TokenColor.WHITE),
        Token(TokenColor.PURPLE),
        Token(TokenColor.PURPLE),
        Token(TokenColor.BLUE),
        Token(TokenColor.BLUE),
        Token(TokenColor.WHITE),
        Token(TokenColor.WHITE)
    )

    private val gems = mutableListOf(
        Gem(GemColor.SAPPHIRE),
        Gem(GemColor.EMERALD),
        Gem(GemColor.EMERALD),
        Gem(GemColor.AMBER),
        Gem(GemColor.AMBER),
        Gem(GemColor.AMBER),
    )

    /**
     * The function [toGameModeTest] test all function of [toGameMode] with alle functionality
     */
    @Test
    fun toGameModeTest() {
        val testGame = RootService()
        assertThrows<IllegalStateException> { (testGame.networkMappingService.toGameMode()) }
        testGame.currentGame = Indigo(
            gameSetting,
            allTiles = placeTiles.toList(),
            gameBoard = GameBoard(),
            gems = gems,
            tokens = tokens
        )
        testGame.currentGame!!.gameBoard.gateTokens = tokens
        var gameMode = testGame.networkMappingService.toGameMode()
        assertEquals(GameMode.THREE_NOT_SHARED_GATEWAYS, gameMode)
        val notSharedTokens = mutableListOf(
            Token(TokenColor.PURPLE),
            Token(TokenColor.PURPLE),
            Token(TokenColor.PURPLE),
            Token(TokenColor.BLUE),
            Token(TokenColor.WHITE),
            Token(TokenColor.WHITE),
            Token(TokenColor.WHITE),
            Token(TokenColor.PURPLE),
            Token(TokenColor.BLUE),
            Token(TokenColor.BLUE),
            Token(TokenColor.BLUE),
            Token(TokenColor.WHITE)
        )
        testGame.currentGame = Indigo(
            gameSetting,
            allTiles = placeTiles.toList(),
            gameBoard = GameBoard(),
            gems = gems,
            tokens = notSharedTokens
        )
        testGame.currentGame!!.gameBoard.gateTokens = notSharedTokens
        gameMode = testGame.networkMappingService.toGameMode()
        assertEquals(GameMode.THREE_SHARED_GATEWAYS, gameMode)
        val fourPlayers = players.toMutableList()
        fourPlayers.add(Player("Charlie", color = TokenColor.RED))
        var gameSettings = GameSettings(fourPlayers.toList())
        val twoPlayers = players.subList(0, 2)
        testGame.currentGame = Indigo(
            gameSettings,
            allTiles = placeTiles.toList(),
            gameBoard = GameBoard(),
            gems = gems,
            tokens = notSharedTokens
        )
        gameMode = testGame.networkMappingService.toGameMode()
        assertEquals(GameMode.FOUR_SHARED_GATEWAYS, gameMode)

        gameSettings = GameSettings(twoPlayers)
        testGame.currentGame = Indigo(
            gameSettings,
            allTiles = placeTiles.toList(),
            gameBoard = GameBoard(),
            gems = gems,
            tokens = notSharedTokens
        )
        gameMode = testGame.networkMappingService.toGameMode()
        assertEquals(GameMode.TWO_NOT_SHARED_GATEWAYS, gameMode)
    }

    /**
     *  The function[toTileTypeListTest] test the function to [toTileTypeList]
     */
    @Test
    fun toTileTypeListTest() {
        val testGame = RootService()
        assertThrows<IllegalStateException> { (testGame.networkMappingService.toGameMode()) }
        testGame.currentGame = Indigo(
            gameSetting,
            allTiles = placeTiles.toList(),
            gameBoard = GameBoard(),
            gems = gems,
            tokens = tokens
        )
        val testTileList = listOf(
            TileType.TYPE_0,
            TileType.TYPE_1,
            TileType.TYPE_2,
            TileType.TYPE_3,
            TileType.TYPE_4
        )
        val tileList = testGame.networkMappingService.toTileTypeList()
        assertEquals(testTileList,tileList)
    }
}