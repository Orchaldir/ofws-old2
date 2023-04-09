package ofws.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SlopeTest {

    val slope0 = Slope(1, 0)
    val slope45 = Slope(1, 1)

    @Nested
    inner class CalculateTopX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..10) {
                assertEquals(i, slope0.calculateTopX(i))
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..10) {
                assertEquals(i, slope45.calculateTopX(i))
            }
        }
    }

    @Nested
    inner class CalculateBottomX {
        @Test
        fun `Test 0 degree slope`() {
            for (i in 0..10) {
                assertEquals(0, slope0.calculateBottomX(i))
            }
        }

        @Test
        fun `Test 45 degree slope`() {
            for (i in 0..10) {
                assertEquals(i, slope45.calculateBottomX(i))
            }
        }
    }
}