package ofws.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SizeTest {

    private val size = Size(2, 3)

    @Test
    fun `Test the number of cells`() {
        assertEquals(6, size.cells)
    }

}