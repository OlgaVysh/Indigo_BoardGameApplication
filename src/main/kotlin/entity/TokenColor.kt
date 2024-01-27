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
            WHITE -> ImageVisual("tokenWhite.png")
            PURPLE -> ImageVisual("tokenPurple.png")
            BLUE -> ImageVisual("tokenBlue.png")
            RED -> ImageVisual("tokenRed.png")
        }
}