package ofws.math.fov

import ofws.math.Position
import ofws.math.map.TileMap
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ShadowCastingTest {

    private val algo = ShadowCasting()

    @Test
    fun `Range 1`() {
        val map = TileMapBuilder(10, 10, false).build()
        val config = createConfig(map, 3, 3, 1)

        val visible = algo.calculateVisibleCells(config)

        assertEquals(9, visible.size)

        assertRectangle(visible, map, 2, 4, 2, 4)
    }

    @Test
    fun `Range 2`() {
        val map = TileMapBuilder(10, 10, false).build()
        val config = createConfig(map, 4, 4, 2)

        val visible = algo.calculateVisibleCells(config)

        assertEquals(25, visible.size)

        assertRectangle(visible, map, 2, 6, 2, 6)
    }

    @Test
    fun `A wall blocks the field of view`() {
        val map = TileMapBuilder(7, 7, false)
            .addRectangle(1, 1, 5, 5, true)
            .build()
        val config = createConfig(map, 4, 4, 10)

        val visible = algo.calculateVisibleCells(config)

        assertEquals(25, visible.size)

        assertRectangle(visible, map, 1, 5, 1, 5)
    }

    @Test
    fun `A single wall next to the watcher`() {
        val map = TileMapBuilder(10, 10, false)
            .addBorder(true)
            .setTile(5, 4, true)
            .build()
        val config = createConfig(map, 4, 4, 10)

        val visible = algo.calculateVisibleCells(config)

        // 1.column
        notVisible(visible, map, 6, 3)
        notVisible(visible, map, 6, 4)
        notVisible(visible, map, 6, 5)

        // 2.column
        notVisible(visible, map, 7, 2)
        notVisible(visible, map, 7, 3)
        notVisible(visible, map, 7, 4)
        notVisible(visible, map, 7, 5)
        notVisible(visible, map, 7, 6)

        // 3.column
        notVisible(visible, map, 8, 1)
        notVisible(visible, map, 8, 2)
        notVisible(visible, map, 8, 3)
        notVisible(visible, map, 8, 4)
        notVisible(visible, map, 8, 5)
        notVisible(visible, map, 8, 6)
        notVisible(visible, map, 8, 7)

        // 4.column
        notVisible(visible, map, 9, 0)
        notVisible(visible, map, 9, 1)
        notVisible(visible, map, 9, 2)
        notVisible(visible, map, 9, 3)
        notVisible(visible, map, 9, 4)
        notVisible(visible, map, 9, 5)
        notVisible(visible, map, 9, 6)
        notVisible(visible, map, 9, 7)
        notVisible(visible, map, 9, 8)
    }

    private fun notVisible(visible: Set<Position>, map: TileMap<Boolean>, x: Int, y: Int) {
        assertFalse(visible.contains(map.size.getPosition(x, y)))
    }

    private fun assertRectangle(visible: Set<Position>, map: TileMap<Boolean>, x0: Int, x1: Int, y0: Int, y1: Int) {
        for (x in x0..x1) {
            for (y in y0..y1) {
                assertTrue(visible.contains(map.size.getPosition(x, y)))
            }
        }
    }

    private fun createConfig(map: TileMap<Boolean>, x: Int, i: Int, range: Int) =
        FovConfig(map.size, map.size.getPosition(x, i), range) { p -> map.terrainList[p.index] }

}