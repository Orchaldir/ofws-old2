package ofws.math.pathfinding

import ofws.math.map.TileIndex

/**
 * The per node information of the [AStarAlgorithm].
 */
data class AStarNode(val index: TileIndex) : Comparable<AStarNode> {
    var costToNode = Int.MAX_VALUE
    var heuristic = 0
    var previous: AStarNode? = null

    override fun compareTo(other: AStarNode) = heuristic.compareTo(other.heuristic)
}