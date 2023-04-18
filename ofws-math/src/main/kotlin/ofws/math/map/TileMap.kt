package ofws.math.map

import ofws.math.Size2d

/**
 * A 2d grid of tiles of type [T].
 */
data class TileMap<T>(
    val size: Size2d,
    val tiles: List<T>,
) {

    fun getTile(index: TileIndex) = tiles[index.index]

    fun builder() = TileMapBuilder(size, tiles.toMutableList())

}