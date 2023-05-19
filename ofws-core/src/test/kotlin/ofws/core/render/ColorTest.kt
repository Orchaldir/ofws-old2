package ofws.core.render

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun `Get red, green & blue as double`() {
        val color = Color(0u, 128u, 255u)

        assertEquals(0.0, color.getRedDouble())
        assertEquals(0.5, color.getGreenDouble(), 0.01)
        assertEquals(1.0, color.getBlueDouble())
    }

    @Nested
    inner class Lerp {

        private val color0 = Color(10u, 30u, 60u)
        private val color1 = Color(20u, 50u, 100u)
        private val result = Color(15u, 40u, 80u)

        @Test
        fun `Interpolate 2 colors`() {
            assertEquals(result, color0.lerp(color1, 0.5))
        }

        @Test
        fun `Interpolate with the higher values first`() {
            assertEquals(result, color1.lerp(color0, 0.5))
        }

        @Test
        fun `Interpolate with 0 returns the start`() {
            assertEquals(color0, color0.lerp(color1, 0.0))
        }

        @Test
        fun `Interpolate with negative values return the start`() {
            assertEquals(color0, color0.lerp(color1, -1.0))
        }

        @Test
        fun `Interpolate with 1 returns the end`() {
            assertEquals(color1, color0.lerp(color1, 1.0))
        }

    }

}