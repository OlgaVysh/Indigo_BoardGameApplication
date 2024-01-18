package entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

/**
 * class modelling a Player
 *
 * @constructor returns a Player with the given customizations
 *
 * @param name [String] for the Player's name
 * @param age (optional) [Date] for the Player's birthdate, defaults to Unix time 0
 * @param color [TokenColor] for the Player's assigned Tokens
 * @param isAI (optional) [Boolean] to check if any given Player is CPU controlled, defaults to false
 * @property score [Int] to keep track of accumulated points, initially 0
 * @property gemCounter [Int] to keep track of accumulated gems, initially 0
 */
open class Player(val name: String, val age: Date = Date(0), var color: TokenColor,
                  @JsonProperty("ai")
                  val isAI: Boolean = false) {
    var score = 0
    var collectedGems = mutableListOf<Gem>()
    var handTile: Tile? = null
}