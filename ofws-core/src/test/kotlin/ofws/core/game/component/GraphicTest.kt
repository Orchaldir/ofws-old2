package ofws.core.game.component

import ofws.core.render.Color
import ofws.core.render.FullTile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GraphicTest {

    private val tile0 = FullTile(Color.BLUE)
    private val tile1 = FullTile(Color.GREEN)
    private val graphic = Graphic(listOf(tile0, tile1))

    @Test
    fun `Test second constructor`() {
        assertEquals(Graphic(listOf(tile0)), Graphic(tile0))
    }

    @Test
    fun `Get the tiles`() {
        assertEquals(tile0, graphic.get(0))
        assertEquals(tile1, graphic.get(1))
    }

    @Test
    fun `Get the default tile for wrong indices`() {
        assertEquals(DEFAULT_GRAPHIC, graphic.get(-1))
        assertEquals(DEFAULT_GRAPHIC, graphic.get(2))
    }

}