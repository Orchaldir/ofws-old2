package ofws.core.game.map

import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.ecs.Entity
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import ofws.math.pathfinding.graph.OccupancyMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GameMapTest {

    private val entity0 = Entity(0)
    private val entity1 = Entity(1)

    private val size = Size2d(3, 5)
    private val blocked = size.getIndex(1, 2)
    private val occupied = size.getIndex(1, 1)

    private val tilemap = TileMapBuilder(size, FLOOR)
        .setTile(blocked, WALL)
        .build()
    private val map = GameMap(tilemap)
    private val mapWithEntity = GameMap(tilemap, EntityMap(size, mapOf(occupied to entity0)))

    @Test
    fun `Test simple constructor`() {
        assertEquals(GameMap(tilemap, EntityMap(size)), GameMap(tilemap))
    }

    @Test
    fun `Get the maps size`() {
        assertEquals(size, map.getSize())
    }

    @Nested
    inner class CheckWalkability {

        @Test
        fun `A wall blocks an entity`() {
            assertEquals(BlockedByObstacle, map.checkWalkability(blocked, entity0))
        }

        @Test
        fun `A floor is walkable`() {
            for (index in (1 until size.tiles).filter { it != blocked.index }) {
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
            assertEquals(Walkable(occupied), mapWithEntity.checkWalkability(occupied, entity0))
        }

        @Test
        fun `Entity is blocked by another entity`() {
            assertEquals(BlockedByEntity(entity0), mapWithEntity.checkWalkability(occupied, entity1, TWO))
        }

        private fun assertWalkable(index: Int) {
            val position = TileIndex(index)
            assertEquals(Walkable(position), map.checkWalkability(position, entity0))
        }

        private fun assertBigIsBlocked(index: Int) {
            assertEquals(BlockedByObstacle, map.checkWalkability(TileIndex(index), entity0, TWO))
        }

        private fun assertBigIsOutside(index: Int) {
            assertEquals(OutsideMap, map.checkWalkability(TileIndex(index), entity0, TWO))
        }
    }

    @Nested
    inner class CreateOccupancyMap {

        @Test
        fun `Entity of size 1 is not blocked by itself`() {
            val occupancyMap = OccupancyMap(
                listOf(
                    true, true, true,
                    true, true, true,
                    true, false, true,
                    true, true, true,
                    true, true, true,
                ), size
            )

            assertEquals(occupancyMap, mapWithEntity.createOccupancyMap(entity0))
        }

        @Test
        fun `Entity of size 1 is blocked by another entity`() {
            val occupancyMap = OccupancyMap(
                listOf(
                    true, true, true,
                    true, false, true,
                    true, false, true,
                    true, true, true,
                    true, true, true,
                ), size
            )

            assertEquals(occupancyMap, mapWithEntity.createOccupancyMap(entity1))
        }

        @Test
        fun `Entity of size 2`() {
            val occupancyMap = OccupancyMap(
                listOf(
                    true, true, false,
                    false, false, false,
                    false, false, false,
                    true, true, false,
                    false, false, false,
                ), size
            )

            assertEquals(occupancyMap, map.createOccupancyMap(entity0, TWO))
        }

    }
}