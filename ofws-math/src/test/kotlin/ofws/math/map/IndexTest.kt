package ofws.math.map

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IndexTest {

    @Test
    fun `test index of index`() {
        assertEquals(6, TileIndex(6).index)
    }

}