package ofws.math.map

import ofws.math.Size
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TileMapBuilderTest {

    @Test
    fun `Create a tile map builder from x, y & tile`() {
        val builder = TileMapBuilder(2, 3, false)
        val result = TileMapBuilder(Size(2, 3), mutableListOf(false, false, false, false, false, false))

        assertEquals(result, builder)
    }

    @Test
    fun `Add a border`() {
        val builder = TileMapBuilder(4, 3, false).addBorder(true)
        val result = TileMapBuilder(
            Size(4, 3), mutableListOf(
                true, true, true, true,
                true, false, false, true,
                true, true, true, true,
            )
        )

        assertEquals(result, builder)
    }

    @Test
    fun `Add a rectangle`() {
        val builder = TileMapBuilder(4, 3, false).addRectangle(1, 0, 3, 3, true)
        val result = TileMapBuilder(
            Size(4, 3), mutableListOf(
                false, true, true, true,
                false, true, false, true,
                false, true, true, true,
            )
        )

        assertEquals(result, builder)
    }

    @Test
    fun `Set a tile`() {
        val builder = TileMapBuilder(2, 3, false).setTile(1, 2, true)
        val result = TileMapBuilder(
            Size(2, 3), mutableListOf(
                false, false,
                false, false,
                false, true,
            )
        )

        assertEquals(result, builder)
    }

    @Test
    fun `Build a map`() {
        val builder = TileMapBuilder(
            Size(2, 3), mutableListOf(
                false, false,
                false, false,
                false, true,
            )
        )
        val map = TileMap(
            Size(2, 3), listOf(
                false, false,
                false, false,
                false, true,
            )
        )

        assertEquals(map, builder.build())
    }

}