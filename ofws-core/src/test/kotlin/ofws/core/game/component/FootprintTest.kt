package ofws.core.game.component

import ofws.math.Direction.*
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class FootprintTest {

    private val size = Size2d(20, 20)
    private val position0 = TileIndex(10)
    private val position1 = TileIndex(30)
    private val position2 = TileIndex(70)
    private val position3 = TileIndex(70)
    private val simple = SimpleFootprint(position0, WEST)
    private val big = BigFootprint(position1, TWO, SOUTH)
    private val snake = SnakeFootprint(listOf(position2, position3), EAST)

    @Nested
    inner class GetIndicesUnderFootprint {

        @Test
        fun `Get the indices of a simple footprint`() {
            assertEquals(listOf(position0), getIndicesUnderFootprint(size, simple))
        }

        @Test
        fun `Get the indices of a big footprint`() {
            val indices = listOf(position1, TileIndex(31), TileIndex(50), TileIndex(51))
            assertEquals(indices, getIndicesUnderFootprint(size, big))
        }

        @Test
        fun `Get the indices of a snake`() {
            assertEquals(listOf(position2, position3), getIndicesUnderFootprint(size, snake))
        }
    }

    @Nested
    inner class GetPosition {

        @Test
        fun `Get the position of a simple footprint`() {
            assertEquals(position0, getPosition(simple))
        }

        @Test
        fun `Get the position of a big footprint`() {
            assertEquals(position1, getPosition(big))
        }

        @Test
        fun `Get the position of a snake`() {
            assertEquals(position2, getPosition(snake))
        }
    }

    @Nested
    inner class GetSize {

        @Test
        fun `Get the size of a simple footprint`() {
            assertEquals(ONE, getSize(simple))
        }

        @Test
        fun `Get the size of a big footprint`() {
            assertEquals(TWO, getSize(big))
        }

        @Test
        fun `Get the size of a snake`() {
            assertEquals(ONE, getSize(snake))
        }
    }

    @Nested
    inner class Update {

        @Test
        fun `Update a simple footprint`() {
            assertEquals(SimpleFootprint(position1, SOUTH), updateFootprint(simple, position1, SOUTH))
        }

        @Test
        fun `Update a big footprint`() {
            assertEquals(BigFootprint(position3, TWO, EAST), updateFootprint(big, position3, EAST))
        }

        @Test
        fun `Update a snake`() {
            val newSnake = SnakeFootprint(listOf(position0, position2), NORTH)
            assertEquals(newSnake, updateFootprint(snake, position0, NORTH))
        }
    }

}