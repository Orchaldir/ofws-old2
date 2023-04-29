package ofws.math.pathfinding.graph

import ofws.math.Size2d
import ofws.math.map.TileIndex

abstract class PathfindingMap(val size: Size2d) : Graph {

    override fun getSize() = size.tiles

    override fun getNeighbors(index: TileIndex): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()
        val (x, y) = size.getPoint(index)

        addNeighbor(neighbors, x + 1, y)
        addNeighbor(neighbors, x, y + 1)
        addNeighbor(neighbors, x - 1, y)
        addNeighbor(neighbors, x, y - 1)

        return neighbors
    }

    abstract fun addNeighbor(neighbors: MutableList<Neighbor>, x: Int, y: Int)

}