package ofws.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Size1dTest {

    @Test
    fun `Size must be at least 1`() {
        for (size in 0 downTo -10) {
            assertThrows(IllegalArgumentException::class.java) { Size1d(size) }
        }
    }

}