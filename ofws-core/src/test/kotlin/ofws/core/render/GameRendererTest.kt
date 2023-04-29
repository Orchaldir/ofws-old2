package ofws.core.render

import io.mockk.Called
import io.mockk.mockk
import io.mockk.verifySequence
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.math.Rectangle
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GameRendererTest {

    private val floorTile = UnicodeTile('.', Color.WHITE)
    private val wallTile = FullTile(Color.WHITE)
    private val tileRenderer = mockk<TileRenderer>(relaxed = true)
    private val size = Size2d(2, 3)
    private val bigMap = GameMap(
        TileMapBuilder(3, 4, FLOOR)
            .setTile(0, 1, WALL)
            .setTile(1, 3, WALL)
            .setTile(2, 0, WALL)
            .build()
    )

    private val renderer = GameRenderer(Rectangle(10, 20, size), tileRenderer)

    @Test
    fun `Simple constructor`() {
        assertEquals(GameRenderer(Rectangle(0, 0, size), tileRenderer), GameRenderer(size, tileRenderer))
    }

    @Nested
    inner class RenderMap {

        @Test
        fun `Render a map inside the area`() {
            val map = GameMap(
                TileMapBuilder(2, 3, FLOOR)
                    .setTile(0, 1, WALL)
                    .build()
            )

            verifyRenderMap(map)
        }

        @Test
        fun `Render too large map`() {
            verifyRenderMap(bigMap)
        }

        private fun verifyRenderMap(map: GameMap) {
            renderer.renderMap(map) { getTile(it) }

            verifySequence {
                tileRenderer.renderTile(floorTile, 10, 20)
                tileRenderer.renderTile(floorTile, 11, 20)
                tileRenderer.renderTile(wallTile, 10, 21)
                tileRenderer.renderTile(floorTile, 11, 21)
                tileRenderer.renderTile(floorTile, 10, 22)
                tileRenderer.renderTile(floorTile, 11, 22)
            }
        }

    }

    @Nested
    inner class RenderTiles {

        @Test
        fun `Render tiles inside the area`() {
            renderer.renderTiles(bigMap, setOf(TileIndex(0), TileIndex(3))) { getTile(it) }

            verifySequence {
                tileRenderer.renderTile(floorTile, 10, 20)
                tileRenderer.renderTile(wallTile, 10, 21)
            }
        }

        @Test
        fun `Skip tiles outside the render area`() {
            renderer.renderTiles(bigMap, setOf(TileIndex(2), TileIndex(12))) { getTile(it) }

            verifySequence { tileRenderer wasNot Called }
        }

    }

    private fun getTile(terrain: Terrain) = if (terrain == FLOOR) {
        floorTile
    } else {
        wallTile
    }

}