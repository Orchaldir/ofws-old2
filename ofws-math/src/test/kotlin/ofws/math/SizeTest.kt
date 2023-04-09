package ofws.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SizeTest {

    private val size = Size(2, 3)

    @Test
    fun `Test the number of tiles inside the area`() {
        assertEquals(6, size.tiles)
    }

    @Nested
    inner class GetPosition {
        @Test
        fun `Convert x & y inside the size to a position`() {
            testGetPosition(0, 0, 0)
            testGetPosition(1, 1, 0)
            testGetPosition(2, 0, 1)
            testGetPosition(3, 1, 1)
            testGetPosition(4, 0, 2)
            testGetPosition(5, 1, 2)
        }

        private fun testGetPosition(index: Int, x: Int, y: Int) {
            assertEquals(Position(index), size.getPosition(x, y))
        }
    }

    @Nested
    inner class GetPositionIfInside {
        @Test
        fun `Convert x & y inside the size to a position`() {
            testGetPosition(0, 0, 0)
            testGetPosition(1, 1, 0)
            testGetPosition(2, 0, 1)
            testGetPosition(3, 1, 1)
            testGetPosition(4, 0, 2)
            testGetPosition(5, 1, 2)
        }

        @Test
        fun `Convert x & y outside the size to null`() {
            testNull(-1, 0)
            testNull(0, -1)
            testNull(-1, -1)
            testNull(2, 2)
            testNull(1, 3)
            testNull(2, 3)
        }

        private fun testGetPosition(index: Int, x: Int, y: Int) {
            assertEquals(Position(index), size.getPositionIfInside(x, y))
        }

        private fun testNull(x: Int, y: Int) {
            assertNull(size.getPositionIfInside(x, y))
        }
    }

    @Nested
    inner class IsInside {
        @Test
        fun `Tiles inside`() {
            testGetPosition(0, 0)
            testGetPosition(1, 0)
            testGetPosition(0, 1)
            testGetPosition(1, 1)
            testGetPosition(0, 2)
            testGetPosition(1, 2)
        }

        @Test
        fun `Tiles outside`() {
            testNull(-1, 0)
            testNull(0, -1)
            testNull(-1, -1)
            testNull(2, 2)
            testNull(1, 3)
            testNull(2, 3)
        }

        @Test
        fun `Test if x is inside`() {
            assertFalse(size.isInsideForX(-3))
            assertFalse(size.isInsideForX(-1))
            assertTrue(size.isInsideForX(0))
            assertTrue(size.isInsideForX(1))
            assertFalse(size.isInsideForX(2))
            assertFalse(size.isInsideForX(3))
        }

        @Test
        fun `Test if y is inside`() {
            assertFalse(size.isInsideForY(-3))
            assertFalse(size.isInsideForY(-1))
            assertTrue(size.isInsideForY(0))
            assertTrue(size.isInsideForY(1))
            assertTrue(size.isInsideForY(2))
            assertFalse(size.isInsideForY(3))
            assertFalse(size.isInsideForY(4))
        }

        private fun testGetPosition(x: Int, y: Int) {
            assertTrue(size.isInside(x, y))
            assertTrue(size.isInsideForX(x))
            assertTrue(size.isInsideForY(y))
        }

        private fun testNull(x: Int, y: Int) {
            assertFalse(size.isInside(x, y))
        }
    }
}