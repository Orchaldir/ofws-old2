package ofws.math.fov

import ofws.math.Position
import ofws.math.Size

data class FovConfig(
    val mapSize: Size,
    val position: Position,
    val range: Int,
    val isBlocking: (position: Position) -> Boolean
)
