package ofws.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class DirectionTest {

    @Nested
    inner class GetDirection {

        @ParameterizedTest
        @EnumSource(Direction::class)
        fun `Each direction gets itself`(d: Direction) {
            assertEquals(d, getDirection(d.x, d.y))
        }

        @Test
        fun `Get null otherwise`() {
            assertNull(getDirection(2, 1))
        }

    }

    @Nested
    inner class IsNeighbor {

        @ParameterizedTest
        @EnumSource(Direction::class)
        fun `Each direction matches itself`(d: Direction) {
            assertTrue(d.isNeighbor(d.x, d.y))
        }

        @ParameterizedTest
        @EnumSource(Direction::class)
        fun `Directions don't match other values`(d: Direction) {
            assertFalse(d.isNeighbor(2, 1))
        }

    }

}