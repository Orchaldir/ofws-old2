package ofws.math.fov

import ofws.math.Range
import ofws.math.Size
import ofws.math.map.TileIndex

data class FovConfig(
    val mapSize: Size,
    val index: TileIndex,
    val range: Range,
    val isBlocking: (index: TileIndex) -> Boolean
)
