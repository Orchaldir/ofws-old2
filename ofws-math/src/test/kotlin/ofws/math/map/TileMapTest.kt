package ofws.math.map

import ofws.math.Size2d
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TileMapTest {

    @Test
    fun `Create a builder of the map`() {
        val map = TileMap(
            Size2d(2, 3), listOf(
                false, false,
                false, false,
                false, true,
            )
        )
        val builder = TileMapBuilder(
            Size2d(2, 3), mutableListOf(
                false, false,
                false, false,
                false, true,
            )
        )


        assertEquals(builder, map.builder())
    }

}