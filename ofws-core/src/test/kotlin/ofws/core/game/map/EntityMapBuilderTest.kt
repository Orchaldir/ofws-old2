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

    private val entity0 = Entity(10)
    private val entity1 = Entity(20)

    private val builder0 = EntityMapBuilder(size)
    private val builder1 = EntityMapBuilder(size, mutableMapOf(index0 to entity0))
    private val builder2 = EntityMapBuilder(
        size, mutableMapOf(
            index0 to entity0,
            index1 to entity0,
            index2 to entity0,
            index3 to entity0,
        )
    )

    @Nested
    inner class AddEntity {

        @Test
        fun `Add an entity`() {
            assertEquals(builder1, builder0.addEntity(index0, entity0))
        }

        @Test
        fun `Add an entity to the same tile again`() {
            assertEquals(builder1, builder1.addEntity(index0, entity0))
        }

        @Test
        fun `An entity can not overwrite another`() {
            assertThrows(IllegalArgumentException::class.java) { builder1.addEntity(index0, entity1) }
        }

    }

    @Nested
    inner class AddBigEntity {

        @Test
        fun `Add an entity`() {
            assertEquals(builder2, builder0.addEntity(index0, entity0, TWO))
        }

        @Test
        fun `Add an entity to the same tile again`() {
            assertEquals(builder2, builder2.addEntity(index0, entity0, TWO))
        }

        @Test
        fun `An entity can not overwrite another`() {
            assertThrows(IllegalArgumentException::class.java) { builder2.addEntity(index2, entity1, TWO) }
        }

    }

    @Nested
    inner class RemoveEntity {

        @Test
        fun `Remove an entity`() {
            assertEquals(builder0, builder1.removeEntity(index0, entity0))
        }

        @Test
        fun `An an entity can not remove another`() {
            assertThrows(IllegalArgumentException::class.java) { builder1.removeEntity(index0, entity1) }
        }

    }

    @Nested
    inner class RemoveBigEntity {

        @Test
        fun `Remove an entity`() {
            assertEquals(builder0, builder2.removeEntity(index0, entity0, TWO))
        }

        @Test
        fun `An an entity can not remove another`() {
            assertThrows(IllegalArgumentException::class.java) { builder2.removeEntity(index0, entity1, TWO) }
        }

    }

}