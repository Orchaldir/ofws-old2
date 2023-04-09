package ofws.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SlopeTest {

    val slope0 = Slope(1, 0)
    val slope45 = Slope(1, 1)
    val slope = Slope(2, 1)

    @Nested
    inner class CalculateTopX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..10) {
                test(slope0, i, 0)
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..10) {
                test(slope45, i, i)
            }
        }

        @Test
        fun `Test another slope`() {
            test(slope, 0, 0)
            test(slope, 1, 1)
            test(slope, 2, 1)
            test(slope, 3, 2)
            test(slope, 4, 2)
            test(slope, 5, 3)
            test(slope, 6, 3)
        }

        private fun test(slope: Slope, localX: Int, result: Int) {
            assertEquals(result, slope.calculateTopX(localX))
        }
    }

    @Nested
    inner class CalculateBottomX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..10) {
                test(slope0, i, 0)
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..10) {
                test(slope45, i, i)
            }
        }

        @Test
        fun `Test another slope`() {
            test(slope, 0, 0)
            test(slope, 1, 0)
            test(slope, 2, 1)
            test(slope, 3, 1)
            test(slope, 4, 2)
            test(slope, 5, 2)
            test(slope, 6, 3)
            test(slope, 7, 3)
        }

        private fun test(slope: Slope, localX: Int, result: Int) {
            assertEquals(result, slope.calculateBottomX(localX))
        }
    }
}