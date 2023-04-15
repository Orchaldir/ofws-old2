package ofws.math

import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class SizeTest {

    private val size = Size(2, 3)

    @Nested
    inner class Constructor {

        @Test
        fun `X must be at least 1`() {
            for (x in 0 downTo -10) {
                assertThrows(IllegalArgumentException::class.java) { Size(x, 10) }
            }
        }

        @Test
        fun `Y must be at least 1`() {
            for (y in 0 downTo -10) {
                assertThrows(IllegalArgumentException::class.java) { Size(20, y) }
            }
        }
    }

    @Test
    fun `Test the number of tiles inside the area`() {
        assertEquals(6, size.tiles)
    }

    @Nested
    inner class GetIndex {
        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Convert x & y inside the size to a index`(index: Int, x: Int, y: Int) {
            assertEquals(TileIndex(index), size.getIndex(x, y))
        }
    }

    @Nested
    inner class GetIndexIfInside {
        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Return a index, if inside`(index: Int, x: Int, y: Int) {
            assertEquals(TileIndex(index), size.getIndexIfInside(x, y)!!)
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#outside")
        fun `Return null if outside`(x: Int, y: Int) {
            assertNull(size.getIndexIfInside(x, y))
        }
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.SizeTest#inside")
    fun `Get a point from index`(index: Int, x: Int, y: Int) {
        assertEquals(Pair(x, y), size.getPoint(TileIndex(index)))
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.SizeTest#inside")
    fun `Get x from index`(index: Int, x: Int, y: Int) {
        assertEquals(x, size.getX(TileIndex(index)))
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.SizeTest#inside")
    fun `Get y from index`(index: Int, x: Int, y: Int) {
        assertEquals(y, size.getY(TileIndex(index)))
    }

    @Nested
    inner class IsInside {

        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Test x & y with inside`(index: Int, x: Int, y: Int) {
            assertTrue(size.isInside(x, y))
        }

        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.SizeTest#inside")
        fun `Test index with inside`(index: Int, x: Int, y: Int) {
            assertTrue(size.isInside(size.getIndex(x, y)))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#outside")
        fun `Test x & y with outside`(x: Int, y: Int) {
            assertFalse(size.isInside(x, y))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.SizeTest#outside")
        fun `Test index with outside`(x: Int, y: Int) {
            assertFalse(size.isInside(size.getIndex(x, y)))
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
            Arguments.of(0, 0, 0),
            Arguments.of(1, 1, 0),
            Arguments.of(2, 0, 1),
            Arguments.of(3, 1, 1),
            Arguments.of(4, 0, 2),
            Arguments.of(5, 1, 2),
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