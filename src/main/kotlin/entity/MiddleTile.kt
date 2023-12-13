package entity

/**
 * class modelling the treasure tile in the middle of an [Indigo] game board
 *
 * @constructor returns a MiddleTile entity with default [Gem] arrangement (Sapphire surrounded by 5 Amber)
 *
 * @property gemPosition [Map] relating positions (0 for middle, >0 for outside) of the [Gem]s on the MiddleTile
 */
class MiddleTile {
    val gemPosition: Map<Int,Gem> = mapOf(
        0 to Gem(GemColor.SAPPHIRE),
        1 to Gem(GemColor.AMBER),
        2 to Gem(GemColor.AMBER),
        3 to Gem(GemColor.AMBER),
        4 to Gem(GemColor.AMBER),
        5 to Gem(GemColor.AMBER))
}