package ofws.core.game.map

import ofws.math.Size2d
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameMapTest {

    private val size = Size2d(3, 4)
    private val tilemap = TileMapBuilder(size, Terrain.FLOOR).build()

    @Test
    fun `Test simple constructor`() {
        assertEquals(GameMap(tilemap, EntityMap(size)), GameMap(tilemap))
    }

}