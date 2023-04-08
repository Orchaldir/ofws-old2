package ofws.core.render

sealed class Tile
object EmptyTile : Tile()
data class FullTile(val color: Color) : Tile()
data class UnicodeTile(val codePoint: Int, val color: Color) : Tile()

fun renderTile(
    renderer: TileRenderer,
    tile: Tile,
    x: Int,
    y: Int,
    size: Int = 1
) {
    when (tile) {
        is FullTile -> renderer.renderFullTile(tile.color, x, y, size)
        is UnicodeTile -> renderer.renderUnicodeTile(tile.codePoint, tile.color, x, y, size)
        is EmptyTile -> return
    }
}