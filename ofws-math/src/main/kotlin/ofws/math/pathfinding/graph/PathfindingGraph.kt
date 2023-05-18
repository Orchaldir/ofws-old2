package ofws.math.pathfinding.graph

import ofws.math.map.TileIndex

/**
 * A graph used by [pathfinding algorithms][ofws.math.pathfinding.PathfindingAlgorithm].
 */
interface PathfindingGraph {

    fun getNodeCount(): Int

    fun isValid(index: TileIndex): Boolean

    fun getNeighbors(index: TileIndex): List<Neighbor>

    fun estimate(from: TileIndex, to: TileIndex): Int

}