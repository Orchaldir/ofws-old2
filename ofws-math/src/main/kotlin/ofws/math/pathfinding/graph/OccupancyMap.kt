package ofws.math.pathfinding.graph

import ofws.math.Size2d
import ofws.math.map.TileIndex

data class OccupancyMap(
    private val occupancy: List<Boolean>,
    private val size: Size2d,
) : PathfindingMap() {
    override fun getNodeCount() = size.tiles
    override fun getSize() = size

    override fun isValid(index: TileIndex) = occupancy[index.index]

    override fun addNeighbor(neighbors: MutableList<Neighbor>, x: Int, y: Int) {
        val index = size.getIndexIfInside(x, y)

        if (index != null && occupancy[index.index]) {
            neighbors.add(Neighbor(index, 1))
        }
    }

    override fun estimate(from: TileIndex, to: TileIndex) = size.getDistance(from, to)

}