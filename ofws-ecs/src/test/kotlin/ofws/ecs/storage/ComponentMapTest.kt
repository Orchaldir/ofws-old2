package ofws.ecs.storage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val TYPE = "Type"

private const val ID0 = 0
private const val ID1 = 9
private const val ID2 = 42
private const val ID3 = 99

private const val A = "A"
private const val B = "B"
private const val C = "C"
private const val D = "D"
private const val E = "E"

private const val NO_ID = 1

private val components: Map<Int, String> = mapOf(ID0 to A, ID1 to B, ID2 to C)
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
            assertEquals(listOf(A, C), storage.getList(ID0, ID2))
        }

        @Test
        fun `Test list with unknown entity`() {
            assertThrows(NoSuchElementException::class.java) { storage.getList(NO_ID) }
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

            assertEquals(D, copy[ID0])
            assertEquals(B, copy[ID1])
            assertEquals(E, copy[ID3])
            assertNull(copy[ID2])
        }

        @Test
        fun `Only update some components`() {
            val copy = storage.updateAndRemove(mapOf(ID0 to E, ID3 to D))

            assertEquals(E, copy[ID0])
            assertEquals(B, copy[ID1])
            assertEquals(C, copy[ID2])
            assertEquals(D, copy[ID3])
        }

    }

}
