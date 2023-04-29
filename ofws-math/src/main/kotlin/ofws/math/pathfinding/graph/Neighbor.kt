package ofws.math.pathfinding.graph

import ofws.math.map.TileIndex

data class Neighbor(
    val index: TileIndex,
    val cost: Int = 0
)