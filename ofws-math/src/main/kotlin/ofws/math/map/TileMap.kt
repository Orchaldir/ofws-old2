package ofws.math.map

import ofws.math.Size

/**
 * A 2d grid of tiles of type [T].
 */
data class TileMap<T>(
    val size: Size,
    val tiles: List<T>,
) {

    fun getTile(index: TileIndex) = tiles[index.index]

    fun builder() = TileMapBuilder(size, tiles.toMutableList())

}