package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size2d
import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EntityMapTest {

    private val map = EntityMapBuilder(Size2d(2, 1))
        .setEntity(TileIndex(1), Entity(42))
        .build()

    @Nested
    inner class GetEntity {

        @Test
        fun `Get no entity`() {
            assertNull(map.getEntity(0, 0))
            assertNull(map.getEntity(2, 0))
        }

        @Test
        fun `Get no entity with index`() {
            assertNull(map.getEntity(TileIndex(0)))
            assertNull(map.getEntity(TileIndex(2)))
        }

        @Test
        fun `Get entity`() {
            assertEquals(Entity(42), map.getEntity(1, 0)!!)
        }

        @Test
        fun `Get entity with index`() {
            assertEquals(Entity(42), map.getEntity(TileIndex(1))!!)
        }
    }

    @Nested
    inner class HasEntity {

        @Test
        fun `Has no entity`() {
            assertFalse(map.hasEntity(0, 0))
            assertFalse(map.hasEntity(2, 0))
        }

        @Test
        fun `Has no entity with index`() {
            assertFalse(map.hasEntity(TileIndex(0)))
            assertFalse(map.hasEntity(TileIndex(2)))
        }

        @Test
        fun `Has entity`() {
            assertTrue(map.hasEntity(1, 0))
        }

        @Test
        fun `Has entity with index`() {
            assertTrue(map.hasEntity(TileIndex(1)))
        }
    }

    @Test
    fun `Create a builder`() {
        assertEquals(map, map.builder().build())
    }

}