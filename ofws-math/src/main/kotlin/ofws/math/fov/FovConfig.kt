package ofws.math.fov

import ofws.math.Size
import ofws.math.map.Index

data class FovConfig(
    val mapSize: Size,
    val index: Index,
    val range: Int,
    val isBlocking: (index: Index) -> Boolean
)
