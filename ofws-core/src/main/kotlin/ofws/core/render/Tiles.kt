package ofws.core.render

import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE

/**
 * A tile defines how each part of a [TileRenderer] is rendered.
 */
sealed class Tile
object EmptyTile : Tile()
data class FullTile(val color: Color) : Tile()
data class UnicodeTile(val codePoint: Int, val color: Color) : Tile() {
    constructor(character: Char, color: Color) : this(character.code, color)
}

fun renderTile(
    renderer: TileRenderer,
    tile: Tile,
    x: Int,
    y: Int,
    size: Size1d = ONE
) {
    when (tile) {
        is FullTile -> renderer.renderFullTile(tile.color, x, y, size)
        is UnicodeTile -> renderer.renderUnicodeTile(tile.codePoint, tile.color, x, y, size)
        is EmptyTile -> return
    }
}