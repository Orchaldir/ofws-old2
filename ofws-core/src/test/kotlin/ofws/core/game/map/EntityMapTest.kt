package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size2d
import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EntityMapTest {

    @Nested
    inner class GetEntity {

        private val map = EntityMapBuilder(Size2d(2, 1))
            .setEntity(TileIndex(1), Entity(42))
            .build()

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

}