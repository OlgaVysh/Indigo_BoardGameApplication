package entity

import tools.aqua.bgw.visual.ImageVisual

/**
 * enum class for the different Token colors
 */
enum class TokenColor {
    WHITE,
    PURPLE,
    BLUE,
    RED,
    ;
    /**
     * function to provide an image to represent this tokenColor.
     * returns the matching image
     */
    fun toImg() =
        when(this) {
            WHITE -> ImageVisual("tokenwhite.png")
            PURPLE -> ImageVisual("tokenpurple.png")
            BLUE -> ImageVisual("tokenblue.png")
            RED -> ImageVisual("tokenred.png")
        }
}