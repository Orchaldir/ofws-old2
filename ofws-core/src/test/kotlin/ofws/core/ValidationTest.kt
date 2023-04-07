package ofws.core

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private const val THRESHOLD = 10

class ValidationTest {

    @Test
    fun `Value is greater than required`() {
        for (i: Int in 11..20) {
            assertEquals(i, requireGreater(i, THRESHOLD, "test"))
        }
    }

    @Test
    fun `Value is less than required`() {
        assertThrows(IllegalArgumentException::class.java) { requireGreater(THRESHOLD, THRESHOLD, "test") }
    }
}