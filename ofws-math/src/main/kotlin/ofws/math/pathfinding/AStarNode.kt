package ofws.math.pathfinding

import ofws.math.map.TileIndex

/**
 * The per node information of the [AStarAlgorithm].
 */
data class AStarNode(
    val index: TileIndex, var costToNode: Int,
    var heuristic: Int,
    var previous: AStarNode?
) : Comparable<AStarNode> {


    constructor(index: TileIndex) : this(index, Int.MAX_VALUE, 0, null)

    override fun compareTo(other: AStarNode) = heuristic.compareTo(other.heuristic)
}