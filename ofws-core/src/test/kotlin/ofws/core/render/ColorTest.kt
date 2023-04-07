package ofws.core.render

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun `Get red, green & blue as double`() {
        val color = Color(0u, 128u, 255u)

        assertEquals(0.0, color.getRedDouble())
        assertEquals(0.5, color.getGreenDouble(), 0.01)
        assertEquals(1.0, color.getBlueDouble())
    }

}