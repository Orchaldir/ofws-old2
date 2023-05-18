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
class AStarAlgorithm(private var nodes: Array<AStarNode?>) : PathfindingAlgorithm {

    constructor(): this(emptyArray())

    fun getCostsToNode(): List<Int?> {
        return nodes.map {
            it?.costToNode
        }
    }

    fun getHeuristics(): List<Int?> {
        return nodes.map {
            it?.heuristic
        }
    }

    override fun find(graph: PathfindingGraph, start: TileIndex, goal: TileIndex, pathSize: Size1d) =
        find(graph, start, setOf(goal), pathSize)

    override fun find(graph: PathfindingGraph, start: TileIndex, goals: Set<TileIndex>, pathSize: Size1d): PathfindingResult {
        logger.debug("Find path from $start to $goals.")

        val openNodes = PriorityQueue<AStarNode>()
        nodes = arrayOfNulls(graph.getNodeCount())
        var isAnyGoalReachable = false

        for (goal in goals) {
            if (start == goal) {
                logger.debug("Goal is already reached")
                return GoalAlreadyReached
            } else if (graph.isValid(goal)) {
                isAnyGoalReachable = true

                val goalNode = AStarNode(goal)
                goalNode.costToNode = 0
                goalNode.heuristic = graph.estimate(goal, start)
                openNodes.add(goalNode)
                nodes[goal.index] = goalNode
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

            updateNeighbors(graph, nodes, openNodes, currentNode, start)
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

            val newCost = currentNode.costToNode + neighbor.cost

            if (newCost < neighborNode.costToNode) {
                neighborNode.costToNode = newCost
                neighborNode.heuristic = newCost + graph.estimate(neighbor.index, start)
                neighborNode.previous = currentNode
                openNodes.add(neighborNode)
            }
        }
    }

    /**
     * Backtrack the path from the start to the nearest goal.
     */
    private fun backtrack(startNode: AStarNode, pathSize: Size1d): Path {
        val indices = mutableListOf<TileIndex>()
        var currentNode: AStarNode? = startNode.previous

        while (currentNode != null) {
            indices.add(currentNode.index)
            currentNode = currentNode.previous
        }

        logger.debug("Found path with ${indices.size} nodes.")
        return Path(size = pathSize, totalCost = startNode.costToNode, indices = indices)
    }
}