package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class EcsStateTest {

    private val boolType = getType(Boolean::class)
    private val intType = getType(Int::class)
    private val stringType = getType(String::class)

    private val intStorage = ComponentMap<Int>(intType)
    private val stringStorage = ComponentMap<String>(stringType)

    @Nested
    inner class GetStorage {

        private val state = EcsState(storageMap = mapOf(intType to intStorage, stringType to stringStorage))

        @Test
        fun `Get existing storage`() {
            assertEquals(intStorage, state.getStorage<Int>())
            assertEquals(stringStorage, state.getStorage<String>())
        }

        @Test
        fun `Get non-existing storage`() {
            assertNull(state.getStorage<Boolean>())
        }

    }

    @Nested
    inner class GetData {

        private val state = EcsState(dataMap = mapOf(intType to 42, stringType to "Test"))

        @Test
        fun `Get existing data`() {
            assertEquals(42, state.getData())
            assertEquals("Test", state.getData<String>())
        }

        @Test
        fun `Get non-existing data`() {
            assertNull(state.getData<Boolean>())
        }

    }


    @Nested
    inner class Copy {

        @Test
        fun `Copy with new storages`() {
            val state = EcsState(storageMap = mapOf(intType to intStorage, stringType to stringStorage))

            val boolStorage = ComponentMap<Boolean>(boolType)
            val newIntStorage = ComponentMap<Int>(intType)

            val copy = state.copy(updatedStorage = listOf(newIntStorage, boolStorage))

            assertNull(state.getStorage<Boolean>())
            assertEquals(intStorage, state.getStorage<Int>())
            assertEquals(stringStorage, state.getStorage<String>())

            assertEquals(boolStorage, copy.getStorage<Boolean>())
            assertEquals(newIntStorage, copy.getStorage<Int>())
            assertEquals(stringStorage, copy.getStorage<String>())
        }

        @Test
        fun `Copy with new data`() {
            val state = EcsState()
            val copy0 = state.copy(updatedData = listOf(42, "Test"))

            assertNull(state.getData<Int>())
            assertNull(state.getData<String>())

            assertEquals(42, copy0.getData())
            assertEquals("Test", copy0.getData<String>())
        }

        @Test
        fun `Copy with overwriting existing data`() {
            val state = EcsState()
            val copy0 = state.copy(updatedData = listOf(42, "Test"))
            val copy1 = copy0.copy(updatedData = listOf(99))

            assertNull(copy0.getData<Boolean>())
            assertEquals(42, copy0.getData())
            assertEquals("Test", copy0.getData<String>())

            assertEquals(99, copy1.getData())
            assertEquals("Test", copy1.getData<String>())
        }

    }
}