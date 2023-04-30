package ofws.math.pathfinding

import ofws.math.Size1d.Companion.ONE
import ofws.math.Size1d.Companion.THREE
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.toList
import ofws.math.map.toSet
import ofws.math.pathfinding.graph.OccupancyMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AStarTest {

    private val f = true
    private val o = false

    private val largeMap = listOf(
        f, f, f, f, f, f,
        f, o, o, o, o, f,
        f, f, o, f, f, f,
        f, f, f, f, f, f,
        f, f, f, f, f, f
    )
    private val largeSize = Size2d(6, 5)
    private val largeGraph = OccupancyMap(largeMap, largeSize)
    private val aStar = AStar()

    @Test
    fun `Find a valid plan`() {
        val result = aStar.find(largeGraph, TileIndex(3), TileIndex(15), TWO)

        assertEquals(Path(TWO, 6, toList(4, 5, 11, 17, 16, 15)), result)
    }

    @Test
    fun `Find a valid plan to the first goal of 2`() {
        val result = aStar.find(largeGraph, TileIndex(2), toSet(13, 15), TWO)

        assertEquals(Path(TWO, 5, toList(1, 0, 6, 12, 13)), result)
    }

    @Test
    fun `Find a valid plan to the second goal of 2`() {
        val result = aStar.find(largeGraph, TileIndex(5), toSet(13, 15), TWO)

        assertEquals(Path(TWO, 4, toList(11, 17, 16, 15)), result)
    }

    @Test
    fun `Start & goal are the same`() {
        val graph = OccupancyMap(listOf(f), Size2d(1, 1))

        assertEquals(GoalAlreadyReached, aStar.find(graph, TileIndex(0), TileIndex(0), ONE))
    }

    @Test
    fun `Blocked by obstacle`() {
        val graph = OccupancyMap(listOf(f, o, f), Size2d(3, 1))

        val result = aStar.find(graph, TileIndex(0), TileIndex(2), ONE)

        assertEquals(NoPathFound(toSet(2), ONE), result)
    }

    @Test
    fun `Start is an obstacle`() {
        val graph = OccupancyMap(listOf(o, f, f), Size2d(3, 1))

        val result = aStar.find(graph, TileIndex(0), TileIndex(2), TWO)

        assertEquals(NoPathFound(toSet(2), TWO), result)
    }

    @Test
    fun `Only goal is an obstacle`() {
        val graph = OccupancyMap(listOf(f, f, o), Size2d(3, 1))

        val result = aStar.find(graph, TileIndex(0), TileIndex(2), THREE)

        assertEquals(NoPathFound(toSet(2), THREE), result)
    }

    @Test
    fun `1 of 2 goals is an obstacle`() {
        val graph = OccupancyMap(listOf(f, f, f, o), Size2d(4, 1))

        val result = aStar.find(graph, TileIndex(2), toSet(0, 3), THREE)

        assertEquals(Path(THREE, 2, toList(1, 0)), result)
    }

    @Test
    fun `All goals are an obstacle`() {
        val graph = OccupancyMap(listOf(o, f, o), Size2d(3, 1))

        val result = aStar.find(graph, TileIndex(1), toSet(0, 2), THREE)

        assertEquals(NoPathFound(toSet(0, 2), THREE), result)
    }

}