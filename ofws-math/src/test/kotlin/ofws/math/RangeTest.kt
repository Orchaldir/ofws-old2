package ofws.math

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class RangeTest {

    @Test
    fun `Min must be at least 1`() {
        for (i in 0 downTo -10) {
            assertThrows(IllegalArgumentException::class.java) { Range(i, 10) }
        }
    }

    @Test
    fun `Max must be at least min`() {
        for (i in 19 downTo 10) {
            assertThrows(IllegalArgumentException::class.java) { Range(20, i) }
        }
    }

}