package ofws.math

import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class RectangleTest {

    private val size = Size2d(3, 4)
    private val rectangle = Rectangle(1, 2, size)

    @Test
    fun `Test simple constructor`() {
        assertEquals(Rectangle(0, 0, size), Rectangle(size))
    }

    @Nested
    inner class ConvertToInside {

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.RectangleTest#outside")
        fun `Tiles outside return null`(x: Int, y: Int) {
            assertNull(rectangle.convertToInside(x, y))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.RectangleTest#inside")
        fun `Tiles inside return an index`(x: Int, y: Int, index: Int) {
            test(x, y, index)
        }

        private fun test(x: Int, y: Int, index: Int) {
            assertEquals(TileIndex(index), rectangle.convertToInside(x, y)!!)
        }

    }

    @Test
    fun `Get the x coordinate of an index`() {
        testGetParentX(6, 1)
        testGetParentX(7, 2)
        testGetParentX(8, 3)
    }

    private fun testGetParentX(index: Int, x: Int) {
        assertEquals(x, rectangle.getParentX(TileIndex(index)))
    }

    @Test
    fun `Get the y coordinate of an index`() {
        testGetParentY(1, 2)
        testGetParentY(4, 3)
        testGetParentY(7, 4)
        testGetParentY(10, 5)
    }

    private fun testGetParentY(index: Int, y: Int) {
        assertEquals(y, rectangle.getParentY(TileIndex(index)))
    }

    @Nested
    inner class IsInside {

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.RectangleTest#outside")
        fun `Test coordinates outside`(x: Int, y: Int) {
            assertFalse(rectangle.isInside(x, y))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.RectangleTest#inside")
        fun `Test coordinates inside`(x: Int, y: Int) {
            assertTrue(rectangle.isInside(x, y))
        }

        @Test
        fun `Test if x is inside`() {
            testGetParentX(0, false)
            testGetParentX(1, true)
            testGetParentX(2, true)
            testGetParentX(3, true)
            testGetParentX(4, false)
        }

        @Test
        fun `Test if y is inside`() {
            testGetParentY(0, false)
            testGetParentY(1, false)
            testGetParentY(2, true)
            testGetParentY(3, true)
            testGetParentY(4, true)
            testGetParentY(5, true)
            testGetParentY(6, false)
        }

        private fun testGetParentX(x: Int, inside: Boolean) {
            assertEquals(inside, rectangle.isInsideForX(x))
        }

        private fun testGetParentY(y: Int, inside: Boolean) {
            assertEquals(inside, rectangle.isInsideForY(y))
        }

    }

    companion object {

        @JvmStatic
        fun inside() = listOf(
            of(1, 2, 0),
            of(2, 2, 1),
            of(3, 2, 2),

            of(1, 3, 3),
            of(2, 3, 4),
            of(3, 3, 5),

            of(1, 4, 6),
            of(2, 4, 7),
            of(3, 4, 8),

            of(1, 5, 9),
            of(2, 5, 10),
            of(3, 5, 11),
        )

        @JvmStatic
        fun outside() = listOf(
            of(0, 0),
            of(1, 0),
            of(2, 0),
            of(3, 0),
            of(4, 0),

            of(0, 1),
            of(1, 1),
            of(2, 1),
            of(3, 1),
            of(4, 1),

            of(0, 2),
            of(4, 2),

            of(0, 3),
            of(4, 3),

            of(0, 4),
            of(4, 4),

            of(0, 5),
            of(4, 5),

            of(0, 6),
            of(1, 6),
            of(2, 6),
            of(3, 6),
            of(4, 6),
        )
    }

}