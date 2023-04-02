package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
    inner class GetStorageOrThrow {

        private val state = EcsState(storageMap = mapOf(intType to intStorage, stringType to stringStorage))

        @Test
        fun `Get existing storage`() {
            assertEquals(intStorage, state.getStorageOrThrow<Int>())
            assertEquals(stringStorage, state.getStorageOrThrow<String>())
        }

        @Test
        fun `Get non-existing storage`() {
            Assertions.assertThrows(NoSuchElementException::class.java) { state.getStorageOrThrow<Boolean>() }
        }

    }

    @Nested
    inner class GetData {

        private val state = EcsState(dataMap = mapOf(intType to 42, stringType to "Test"))

        @Test
        fun `Get existing data`() {
            assertData(state, 42, "Test")
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

            assertStorage(state, null, newIntStorage, stringStorage)
            assertStorage(copy, boolStorage, newIntStorage, stringStorage)
        }

        @Test
        fun `Copy with new data`() {
            val state = EcsState()
            val copy0 = state.copy(updatedData = listOf(42, "Test"))

            assertData(state, null, null)
            assertData(copy0, 42, "Test")
        }

        @Test
        fun `Copy with overwriting existing data`() {
            val state = EcsState()
            val copy0 = state.copy(updatedData = listOf(42, "Test"))
            val copy1 = copy0.copy(updatedData = listOf(99))

            assertData(copy0, 42, "Test")
            assertData(copy1, 99, "Test")
        }

    }

    private fun assertData(state: EcsState, intData: Int?, stringData: String?) {
        assertEquals(intData, state.getData())
        assertEquals(stringData, state.getData<String>())
    }

    private fun assertStorage(
        state: EcsState,
        boolStorage: ComponentMap<Boolean>?,
        intStorage: ComponentMap<Int>?,
        stringStorage: ComponentMap<String>?,
    ) {
        assertEquals(boolStorage, state.getStorage<Boolean>())
        assertEquals(intStorage, state.getStorage<Int>())
        assertEquals(stringStorage, state.getStorage<String>())
    }
}