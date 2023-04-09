package ofws.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

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
        fun `Return a position, if inside`() {
            testGetPosition(0, 0, 0)
            testGetPosition(1, 1, 0)
            testGetPosition(2, 0, 1)
            testGetPosition(3, 1, 1)
            testGetPosition(4, 0, 2)
            testGetPosition(5, 1, 2)
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#outside")
        fun `Return null if outside`(x: Int, y: Int) {
            assertNull(size.getPositionIfInside(x, y))
        }

        private fun testGetPosition(index: Int, x: Int, y: Int) {
            assertEquals(Position(index), size.getPositionIfInside(x, y))
        }
    }

    @Nested
    inner class IsInside {

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Test x & y with inside`(x: Int, y: Int) {
            assertTrue(size.isInside(x, y))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Test position with inside`(x: Int, y: Int) {
            assertTrue(size.isInside(size.getPosition(x, y)))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#outside")
        fun `Tiles outside`(x: Int, y: Int) {
            assertFalse(size.isInside(x, y))
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
    }

    companion object {
        @JvmStatic
        fun inside() = listOf(
            Arguments.of(0, 0),
            Arguments.of(1, 0),
            Arguments.of(0, 1),
            Arguments.of(1, 1),
            Arguments.of(0, 2),
            Arguments.of(1, 2),
        )

        @JvmStatic
        fun outside() = listOf(
            Arguments.of(-1, 0),
            Arguments.of(0, -1),
            Arguments.of(-1, -1),
            Arguments.of(2, 2),
            Arguments.of(1, 3),
            Arguments.of(2, 3),
        )
    }
}