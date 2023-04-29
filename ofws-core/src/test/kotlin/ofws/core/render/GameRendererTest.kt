package ofws.core.render

import io.mockk.Called
import io.mockk.mockk
import io.mockk.verifySequence
import ofws.core.game.component.*
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.core.render.Color.Companion.BLUE
import ofws.core.render.Color.Companion.WHITE
import ofws.ecs.EcsBuilder
import ofws.math.Rectangle
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GameRendererTest {

    private val entityTile = UnicodeTile('@', BLUE)
    private val floorTile = UnicodeTile('.', WHITE)
    private val wallTile = FullTile(WHITE)
    private val tileRenderer = mockk<TileRenderer>(relaxed = true)
    private val size = Size2d(2, 3)
    private val bigSize = Size2d(3, 4)
    private val bigMap = GameMap(
        TileMapBuilder(bigSize, FLOOR)
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
    inner class RenderEntities {

        @Test
        fun `Render a simple footprint`() {
            val state = with(EcsBuilder()) {
                createEntity()
                    .add(SimpleFootprint(TileIndex(4)) as Footprint)
                    .add(Graphic(entityTile))
                build()
            }

            renderer.renderEntities(state, bigSize)

            verifySequence {
                tileRenderer.renderTile(entityTile, 11, 21)
            }
        }

        @Test
        fun `Render a big footprint`() {
            val state = with(EcsBuilder()) {
                createEntity()
                    .add(BigFootprint(TileIndex(1), TWO) as Footprint)
                    .add(Graphic(entityTile))
                build()
            }

            renderer.renderEntities(state, bigSize)

            verifySequence {
                tileRenderer.renderTile(entityTile, 11, 20, TWO)
            }
        }

        @Test
        fun `Render a snake footprint`() {
            val state = with(EcsBuilder()) {
                createEntity()
                    .add(SnakeFootprint(listOf(TileIndex(1), TileIndex(4))) as Footprint)
                    .add(Graphic(entityTile))
                build()
            }

            renderer.renderEntities(state, bigSize)

            verifySequence {
                tileRenderer.renderTile(entityTile, 11, 20)
                tileRenderer.renderTile(entityTile, 11, 21)
            }
        }

        @Test
        fun `Skip entity outside the render area`() {
            val state = with(EcsBuilder()) {
                createEntity()
                    .add(SimpleFootprint(TileIndex(2)) as Footprint)
                    .add(Graphic(entityTile))
                build()
            }

            renderer.renderEntities(state, bigSize)

            verifySequence { tileRenderer wasNot Called }
        }

    }

    @Nested
    inner class RenderMap {

        @Test
        fun `Render a map inside the render area`() {
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
        fun `Render tiles inside the render area`() {
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