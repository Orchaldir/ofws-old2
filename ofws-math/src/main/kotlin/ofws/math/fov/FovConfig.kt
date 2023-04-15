package ofws.math.fov

import ofws.math.Size
import ofws.math.map.TileIndex

data class FovConfig(
    val mapSize: Size,
    val index: TileIndex,
    val range: Int,
    val isBlocking: (index: TileIndex) -> Boolean
)
