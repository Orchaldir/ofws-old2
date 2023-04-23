package ofws.core.game.map

import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.ecs.Entity
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GameMapTest {

    private val size = Size2d(3, 5)

    @Test
    fun `Test simple constructor`() {
        val tilemap = TileMapBuilder(size, FLOOR).build()
        assertEquals(GameMap(tilemap, EntityMap(size)), GameMap(tilemap))
    }

    @Nested
    inner class CheckWalkability {

        private val blocked = TileIndex(0)
        private val index4 = TileIndex(4)

        private val entity0 = Entity(0)
        private val entity1 = Entity(1)

        private val tilemap = TileMapBuilder(3, 5, FLOOR)
            .setTile(1, 2, WALL)
            .build()
        private val map = GameMap(tilemap)
        private val mapWithEntity = GameMap(tilemap, EntityMap(size, mapOf(index4 to entity0)))

        @Test
        fun `A wall blocks an entity`() {
            assertEquals(BlockedByObstacle, map.checkWalkability(blocked, entity0))
        }

        @Test
        fun `A floor is walkable`() {
            for (index in (1 until size.tiles).filter { it != 7 }) {
                assertWalkable(index)
            }
        }

        @Test
        fun `A wall blocks a big entity`() {
            assertBigIsBlocked(3)
            assertBigIsBlocked(4)
            assertBigIsBlocked(6)
            assertBigIsBlocked(7)
        }

        @Test
        fun `An entity is outside`() {
            assertEquals(OutsideMap, map.checkWalkability(TileIndex(15), entity0))
        }

        @Test
        fun `A big entity is outside`() {
            assertBigIsOutside(2)
            assertBigIsOutside(5)
            assertBigIsOutside(8)
            assertBigIsOutside(12)
            assertBigIsOutside(13)
            assertBigIsOutside(14)
        }

        @Test
        fun `Entity can walk in its own cell`() {
            assertEquals(Walkable(index4), mapWithEntity.checkWalkability(index4, entity0, TWO))
        }

        @Test
        fun `Entity is blocked by another entity`() {
            assertEquals(BlockedByEntity(entity0), mapWithEntity.checkWalkability(index4, entity1, TWO))
        }

        private fun assertWalkable(index: Int) {
            val position = TileIndex(index)
            assertEquals(Walkable(position), map.checkWalkability(position, entity0, TWO))
        }

        private fun assertBigIsBlocked(index: Int) {
            assertEquals(BlockedByObstacle, map.checkWalkability(TileIndex(index), entity0, TWO))
        }

        private fun assertBigIsOutside(index: Int) {
            assertEquals(OutsideMap, map.checkWalkability(TileIndex(index), entity0, TWO))
        }
    }
}