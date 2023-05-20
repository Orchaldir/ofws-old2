package ofws.math.pathfinding

import ofws.math.Size1d
import ofws.math.map.TileIndex
import ofws.math.pathfinding.graph.PathfindingGraph

/**
 * An algorithm for pathfinding .
 */
interface PathfindingAlgorithm {
    fun find(graph: PathfindingGraph, start: TileIndex, goal: TileIndex, pathSize: Size1d): PathfindingResult
    fun find(graph: PathfindingGraph, start: TileIndex, goals: Set<TileIndex>, pathSize: Size1d): PathfindingResult
}