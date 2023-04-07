package ofws.core.render

sealed class Tile
object EmptyTile : Tile()
data class FullTile(val color: Color) : Tile()
data class UnicodeTile(val symbol: String, val color: Color) : Tile()