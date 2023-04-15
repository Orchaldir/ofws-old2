package ofws.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val N = 10

class SlopeTest {

    val slope0 = Slope(1, 0)
    val slope45 = Slope(1, 1)
    val slope = Slope(2, 1)

    @Nested
    inner class CalculateTopX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..N) {
                test(slope0, i, 0)
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..N) {
                test(slope45, i, i)
            }
        }

        @Test
        fun `Test another slope`() {
            for (i in 0..N) {
                test(slope, i, (i + 1) / 2)
            }
        }

        private fun test(slope: Slope, localX: Int, result: Int) {
            assertEquals(result, slope.calculateTopX(localX))
        }
    }

    @Nested
    inner class CalculateBottomX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..N) {
                test(slope0, i, 0)
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..N) {
                test(slope45, i, i)
            }
        }

        @Test
        fun `Test another slope`() {
            for (i in 0..N) {
                test(slope, i, i / 2)
            }
        }

        private fun test(slope: Slope, localX: Int, result: Int) {
            assertEquals(result, slope.calculateBottomX(localX))
        }
    }

    @Nested
    inner class CreateSlopeThroughTopLeft {
        @Test
        fun `Create Slope Through Top Left`() {
            test(1, 0, 1, 1)
            test(1, 1, 1, 3)
            test(2, 0, 3, 1)
        }

        private fun test(x: Int, y: Int, resultX: Int, resultY: Int) {
            assertEquals(Slope(resultX, resultY), createSlopeThroughTopLeft(x, y))
        }
    }

    @Nested
    inner class CreateSlopeThroughTopRight {
        @Test
        fun `Create Slope Through Top Right`() {
            test(1, 0, 3, 1)
            test(1, 1, 3, 3)
            test(2, 0, 5, 1)
        }

        private fun test(x: Int, y: Int, resultX: Int, resultY: Int) {
            assertEquals(Slope(resultX, resultY), createSlopeThroughTopRight(x, y))
        }
    }
}