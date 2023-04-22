package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EntityMapBuilderTest {

    private val size = Size2d(2, 3)

    private val index0 = TileIndex(0)
    private val index1 = TileIndex(1)
    private val index2 = TileIndex(2)
    private val index3 = TileIndex(3)
    private val outside = TileIndex(100)

    private val entity = Entity(10)
    private val other = Entity(20)

    private val builder0 = EntityMapBuilder(size)
    private val builder1 = EntityMapBuilder(size, mutableMapOf(index0 to entity))
    private val builder2 = EntityMapBuilder(
        size, mutableMapOf(
            index0 to entity,
            index1 to entity,
            index2 to entity,
            index3 to entity,
        )
    )

    @Nested
    inner class AddEntity {

        @Test
        fun `Add an entity`() {
            assertEquals(builder1, builder0.addEntity(index0, entity))
        }

        @Test
        fun `Add an entity outside the map`() {
            assertThrows(IllegalArgumentException::class.java) { builder0.addEntity(outside, entity) }
        }

        @Test
        fun `Add an entity to the same tile again`() {
            assertEquals(builder1, builder1.addEntity(index0, entity))
        }

        @Test
        fun `An entity can not overwrite another`() {
            assertThrows(IllegalArgumentException::class.java) { builder1.addEntity(index0, other) }
        }

    }

    @Nested
    inner class AddBigEntity {

        @Test
        fun `Add an entity`() {
            assertEquals(builder2, builder0.addEntity(index0, entity, TWO))
        }

        @Test
        fun `Add an entity outside the map`() {
            assertThrows(IllegalArgumentException::class.java) { builder0.addEntity(outside, entity, TWO) }
        }

        @Test
        fun `Add an entity to the same tile again`() {
            assertEquals(builder2, builder2.addEntity(index0, entity, TWO))
        }

        @Test
        fun `An entity can not overwrite another`() {
            assertThrows(IllegalArgumentException::class.java) { builder2.addEntity(index2, other, TWO) }
        }

    }

    @Nested
    inner class RemoveEntity {

        @Test
        fun `Remove an entity`() {
            assertEquals(builder0, builder1.removeEntity(index0, entity))
        }

        @Test
        fun `Exception if there is no entity to remove`() {
            assertThrows(IllegalArgumentException::class.java) { builder1.removeEntity(index1, entity) }
        }

        @Test
        fun `One entity can not remove another`() {
            assertThrows(IllegalArgumentException::class.java) { builder1.removeEntity(index0, other) }
        }

    }

    @Nested
    inner class RemoveBigEntity {

        @Test
        fun `Remove an entity`() {
            assertEquals(builder0, builder2.removeEntity(index0, entity, TWO))
        }

        @Test
        fun `Exception if there is no entity to remove`() {
            assertThrows(IllegalArgumentException::class.java) { builder2.removeEntity(index2, entity, TWO) }
        }

        @Test
        fun `One entity can not remove another`() {
            assertThrows(IllegalArgumentException::class.java) { builder2.removeEntity(index0, other, TWO) }
        }

    }

}