package ofws.math.pathfinding

import mu.KotlinLogging
import ofws.math.Size1d
import ofws.math.map.TileIndex
import ofws.math.pathfinding.graph.PathfindingGraph
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * A famous [pathfinding algorithm][PathfindingAlgorithm].
 * See [wikipedia](https://en.wikipedia.org/wiki/A*_search_algorithm).
 */
class AStar : PathfindingAlgorithm {

    override fun find(graph: PathfindingGraph, start: TileIndex, goal: TileIndex, pathSize: Size1d) =
        find(graph, start, setOf(goal), pathSize)

    override fun find(graph: PathfindingGraph, start: TileIndex, goals: Set<TileIndex>, pathSize: Size1d): PathfindingResult {
        logger.debug("Find path from $start to $goals.")

        val openNodes = PriorityQueue<AStarNode>()
        val list = arrayOfNulls<AStarNode>(graph.getNodeCount())
        var isAnyGoalReachable = false

        for (goal in goals) {
            if (start == goal) {
                logger.debug("Goal is already reached")
                return GoalAlreadyReached
            } else if (graph.isValid(goal)) {
                isAnyGoalReachable = true

                val goalNode = AStarNode(goal)
                goalNode.costSoFar = 0
                openNodes.add(goalNode)
                list[goal.index] = goalNode
            }
        }

        if (!isAnyGoalReachable) {
            logger.debug("All goals are an obstacle")
            return NoPathFound(goals = goals, size = pathSize)
        }

        while (!openNodes.isEmpty()) {
            val currentNode = openNodes.poll()

            if (currentNode.index == start) {
                return backtrack(currentNode, pathSize)
            }

            updateNeighbors(graph, list, openNodes, currentNode, start)
        }

        logger.debug("Failed to find a path.")

        return NoPathFound(goals = goals, size = pathSize)
    }

    private fun updateNeighbors(
        graph: PathfindingGraph,
        list: Array<AStarNode?>,
        openNodes: PriorityQueue<AStarNode>,
        currentNode: AStarNode,
        start: TileIndex
    ) {
        for (neighbor in graph.getNeighbors(currentNode.index)) {
            var neighborNode = list[neighbor.index.index]

            if (neighborNode == null) {
                neighborNode = AStarNode(neighbor.index)
                list[neighbor.index.index] = neighborNode
            }

            val newCost = currentNode.costSoFar + neighbor.cost

            if (newCost < neighborNode.costSoFar) {
                neighborNode.costSoFar = newCost
                neighborNode.heuristic = newCost + graph.estimate(neighbor.index, start)
                neighborNode.previous = currentNode
                openNodes.add(neighborNode)
            }
        }
    }

    private fun backtrack(startNode: AStarNode, pathSize: Size1d): Path {
        val indices = mutableListOf<TileIndex>()
        var currentNode: AStarNode? = startNode.previous

        while (currentNode != null) {
            indices.add(currentNode.index)
            currentNode = currentNode.previous
        }

        logger.debug("Found path with ${indices.size} nodes.")
        return Path(size = pathSize, totalCost = startNode.costSoFar, indices = indices)
    }

    private data class AStarNode(val index: TileIndex) : Comparable<AStarNode> {
        var costSoFar = Int.MAX_VALUE
        var heuristic = 0
        var previous: AStarNode? = null

        override fun compareTo(other: AStarNode) = heuristic.compareTo(other.heuristic)
    }
}