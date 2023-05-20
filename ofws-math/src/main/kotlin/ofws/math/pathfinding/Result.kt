package ofws.math.pathfinding

import ofws.math.Size1d
import ofws.math.map.TileIndex

/**
 * The result of a [pathfinding algorithm][PathfindingAlgorithm].
 */
sealed class PathfindingResult
object GoalAlreadyReached : PathfindingResult()
data class NoPathFound(val goals: Set<TileIndex>, val size: Size1d) : PathfindingResult()
object NotSearched : PathfindingResult()
data class Path(val size: Size1d, val totalCost: Int, val indices: List<TileIndex>) : PathfindingResult()