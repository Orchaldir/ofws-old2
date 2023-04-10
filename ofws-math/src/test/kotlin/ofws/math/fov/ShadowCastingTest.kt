package ofws.math.fov

import ofws.math.Position
import ofws.math.map.TileMap
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ShadowCastingTest {

    private val algo = ShadowCasting()

    @Test
    fun `All walls of a room are visible`() {
        val map = TileMapBuilder(5, 6, false).addBorder(true).build()
        val config = createConfig(map, 2, 3)

        val visible = algo.calculateVisibleCells(config)

        assertEquals(map.size.tiles, visible.size)

        for (i in 0 until map.size.tiles) {
            assertTrue(visible.contains(Position(i)))
        }
    }

    @Test
    fun `A wall blocks the field of view`() {
        val map = TileMapBuilder(7, 7, false)
            .addRectangle(1, 1, 5, 5, true)
            .build()
        val config = createConfig(map, 4, 4)

        val visible = algo.calculateVisibleCells(config)

        assertEquals(25, visible.size)

        for (x in 1 .. 5) {
            for (y in 1 .. 5) {
                assertTrue(visible.contains(map.size.getPosition(x, y)))
            }
        }
    }

    private fun createConfig(map: TileMap<Boolean>, x: Int, i: Int) =
        FovConfig(map.size, map.size.getPosition(x, i), 10) { p -> map.terrainList[p.index] }

}