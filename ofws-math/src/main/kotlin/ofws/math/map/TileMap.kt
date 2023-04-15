package ofws.math.map

import ofws.math.Position
import ofws.math.Size


data class TileMap<T>(
    val size: Size,
    val tiles: List<T>,
) {

    fun getTile(position: Position) = tiles[position.index]

    fun builder() = TileMapBuilder(size, tiles.toMutableList())

}