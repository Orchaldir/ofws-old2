package ofws.math.map

import ofws.math.Size


data class TileMap<T>(
    val size: Size,
    val terrainList: List<T>,
) {

    fun builder() = TileMapBuilder(size, terrainList.toMutableList())

}