package ofws.math

import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

class Size2dTest {

    private val size = Size2d(2, 3)

    @Nested
    inner class Constructor {

        @Test
        fun `X must be at least 1`() {
            for (x in 0 downTo -10) {
                assertThrows(IllegalArgumentException::class.java) { Size2d(x, 10) }
            }
        }

        @Test
        fun `Y must be at least 1`() {
            for (y in 0 downTo -10) {
                assertThrows(IllegalArgumentException::class.java) { Size2d(20, y) }
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
        @MethodSource("ofws.math.Size2dTest#inside")
        fun `Convert x & y inside the size to a index`(index: Int, x: Int, y: Int) {
            assertEquals(TileIndex(index), size.getIndex(x, y))
        }
    }

    @Nested
    inner class GetIndexIfInside {
        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.Size2dTest#inside")
        fun `Return a index, if inside`(index: Int, x: Int, y: Int) {
            assertEquals(TileIndex(index), size.getIndexIfInside(x, y)!!)
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.Size2dTest#outside")
        fun `Return null if outside`(x: Int, y: Int) {
            assertNull(size.getIndexIfInside(x, y))
        }
    }

    @Nested
    inner class GetIndices {

        private val size = Size2d(3, 4)

        @Test
        fun `Get indices with size 2`() {
            test(0, 2, listOf(0, 1, 3, 4))
            test(4, 2, listOf(4, 5, 7, 8))
        }

        @Test
        fun `Get indices with start outside`() {
            test(-1, 3, listOf())
        }

        @Test
        fun `Get indices with end outside`() {
            test(1, 3, listOf())
        }

        private fun test(index: Int, size: Int, result: List<Int>) {
            assertEquals(result.map { TileIndex(it) }, this.size.getIndices(TileIndex(index), Size1d(size)))
        }
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.Size2dTest#inside")
    fun `Get a point from index`(index: Int, x: Int, y: Int) {
        assertEquals(Pair(x, y), size.getPoint(TileIndex(index)))
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.Size2dTest#inside")
    fun `Get x from index`(index: Int, x: Int, y: Int) {
        assertEquals(x, size.getX(TileIndex(index)))
    }

    @ParameterizedTest(name = "index={0} x={1} y={2}")
    @MethodSource("ofws.math.Size2dTest#inside")
    fun `Get y from index`(index: Int, x: Int, y: Int) {
        assertEquals(y, size.getY(TileIndex(index)))
    }

    @Nested
    inner class IsInside {

        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.Size2dTest#inside")
        fun `Test x & y with inside`(index: Int, x: Int, y: Int) {
            assertTrue(size.isInside(x, y))
        }

        @ParameterizedTest(name = "index={0} x={1} y={2}")
        @MethodSource("ofws.math.Size2dTest#inside")
        fun `Test index with inside`(index: Int, x: Int, y: Int) {
            assertTrue(size.isInside(size.getIndex(x, y)))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.Size2dTest#outside")
        fun `Test x & y with outside`(x: Int, y: Int) {
            assertFalse(size.isInside(x, y))
        }

        @ParameterizedTest(name = "x={0} y={1}")
        @MethodSource("ofws.math.Size2dTest#outside")
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

    @Nested
    inner class IsRectangleInside {

        private val size2d = Size2d(3, 4)
        private val size1d = Size1d(2)

        @Test
        fun `Area is inside`() {
            test(0, true)
            test(4, true)
        }

        @Test
        fun `Index is outside`() {
            test(-1, false)
            test(12, false)
        }

        private fun test(index: Int, result: Boolean) {
            assertEquals(result, size2d.isInside(TileIndex(index), size1d))
        }
    }

    companion object {
        @JvmStatic
        fun inside() = listOf(
            of(0, 0, 0),
            of(1, 1, 0),
            of(2, 0, 1),
            of(3, 1, 1),
            of(4, 0, 2),
            of(5, 1, 2),
        )

        @JvmStatic
        fun outside() = listOf(
            of(-1, 0),
            of(0, -1),
            of(-1, -1),
            of(2, 2),
            of(1, 3),
            of(2, 3),
        )
    }
}