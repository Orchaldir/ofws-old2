package ofws.math.pathfinding.graph

import ofws.math.Size2d
import ofws.math.map.TileIndex

/**
 * A [PathfindingGraph] that works with [tilemaps][ofws.math.map.TileMap].
 */
abstract class PathfindingMap : PathfindingGraph {

    abstract fun getSize(): Size2d

    override fun getNeighbors(index: TileIndex): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()
        val (x, y) = getSize().getPoint(index)

        addNeighbor(neighbors, x + 1, y)
        addNeighbor(neighbors, x, y + 1)
        addNeighbor(neighbors, x - 1, y)
        addNeighbor(neighbors, x, y - 1)

        return neighbors
    }

    abstract fun addNeighbor(neighbors: MutableList<Neighbor>, x: Int, y: Int)

}