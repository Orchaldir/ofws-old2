package ofws.math

import ofws.math.Octant.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class OctantTest {

    @Nested
    inner class GetGlobal {
        @Test
        fun `Test North`() {
            test(NORTH, 11, 22)
        }

        @Test
        fun `Test North East`() {
            test(NORTH_EAST, 12, 21)
        }

        @Test
        fun `Test East`() {
            test(EAST, 12, 19)
        }

        @Test
        fun `Test South East`() {
            test(SOUTH_EAST, 11, 18)
        }

        @Test
        fun `Test South`() {
            test(SOUTH, 9, 18)
        }

        @Test
        fun `Test South West`() {
            test(SOUTH_WEST, 8, 19)
        }

        @Test
        fun `Test West`() {
            test(WEST, 8, 21)
        }

        @Test
        fun `Test North West`() {
            test(NORTH_WEST, 9, 22)
        }

        private fun test(octant: Octant, x: Int, y: Int) {
            assertEquals(Pair(x, y), octant.getGlobal(10, 20, 2, 1))
        }
    }

}