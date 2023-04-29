package ofws.core.render

/**
 * A tile defines how each part of a [TileRenderer] is rendered.
 */
sealed class Tile
object EmptyTile : Tile()
data class FullTile(val color: Color) : Tile()
data class UnicodeTile(val codePoint: Int, val color: Color) : Tile() {
    constructor(character: Char, color: Color) : this(character.code, color)
}
