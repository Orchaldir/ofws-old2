package ofws.ecs.storage

import ofws.ecs.Entity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val TYPE = "Type"

private val ID0 = Entity(0)
private val ID1 = Entity(9)
private val ID2 = Entity(42)
private val ID3 = Entity(99)
private val NO_ID = Entity(1)

private const val A = "A"
private const val B = "B"
private const val C = "C"
private const val D = "D"
private const val E = "E"


private val components: Map<Entity, String> = mapOf(ID0 to A, ID1 to B, ID2 to C)
private val storage = ComponentMap(TYPE, components)

class ComponentMapTest {

    @Test
    fun `Get correct type`() {
        assertEquals(TYPE, storage.getType())
    }

    @Nested
    inner class Has {

        @Test
        fun `Test has() with known id`() {
            assertTrue(storage.has(ID0))
            assertTrue(storage.has(ID1))
            assertTrue(storage.has(ID2))
        }

        @Test
        fun `Test has() with unknown id`() {
            assertFalse(storage.has(NO_ID))
        }

    }

    @Nested
    inner class Get {

        @Test
        fun `Test get() with known id`() {
            assertEquals(A, storage[ID0])
            assertEquals(B, storage[ID1])
            assertEquals(C, storage[ID2])
        }

        @Test
        fun `Test get() with unknown id`() {
            assertNull(storage[NO_ID])
        }

    }

    @Nested
    inner class GetOrThrow {

        @Test
        fun `Test getOrThrow() with known id`() {
            assertEquals(A, storage.getOrThrow(ID0))
            assertEquals(B, storage.getOrThrow(ID1))
            assertEquals(C, storage.getOrThrow(ID2))
        }

        @Test
        fun `Test getOrThrow() with unknown id`() {
            assertThrows(NoSuchElementException::class.java) { storage.getOrThrow(NO_ID) }
        }

    }

    @Nested
    inner class GetList {

        @Test
        fun `Test list of known entities`() {
            assertEquals(listOf(A, C), storage.getList(listOf(ID0, ID2)))
        }

        @Test
        fun `Test list with unknown entity`() {
            assertThrows(NoSuchElementException::class.java) { storage.getList(listOf(NO_ID)) }
        }

    }

    @Test
    fun `Get all components`() {
        assertIterableEquals(listOf(A, B, C), storage.getAll())
    }

    @Test
    fun `Get the ids of all entities with this component`() {
        assertEquals(setOf(ID0, ID1, ID2), storage.getIds())
    }

    @Nested
    inner class UpdateAndRemove {

        @Test
        fun `Update and remove some components`() {
            val copy = storage.updateAndRemove(mapOf(ID0 to D, ID3 to E), setOf(ID2))

            assertEquals(ComponentMap(TYPE, mapOf(ID0 to D, ID1 to B, ID3 to E)), copy)
        }

        @Test
        fun `Only update some components`() {
            val copy = storage.updateAndRemove(mapOf(ID0 to E, ID3 to D))

            assertEquals(ComponentMap(TYPE, mapOf(ID0 to E, ID1 to B, ID2 to C, ID3 to D)), copy)
        }

        @Test
        fun `Only remove some components`() {
            val copy = storage.updateAndRemove(removed = setOf(ID0))

            assertEquals(ComponentMap(TYPE, mapOf(ID1 to B, ID2 to C)), copy)
        }

    }

}
