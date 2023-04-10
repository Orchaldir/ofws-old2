package ofws.math

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PositionTest {

    @Test
    fun `test index of position`() {
        assertEquals(6, Position(6).index)
    }

}