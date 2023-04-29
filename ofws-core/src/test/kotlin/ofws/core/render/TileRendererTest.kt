package ofws.core.render

import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import ofws.core.render.Color.Companion.GREEN
import ofws.core.render.Color.Companion.RED
import ofws.math.Size1d.Companion.FOUR
import ofws.math.Size1d.Companion.THREE
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TileRendererTest {

    private val renderer = mockk<Renderer>(relaxed = true)
    private val tileRenderer = TileRenderer(renderer, 100, 200, Size2d(30, 40))

    @Nested
    inner class GetTileX {

        @Test
        fun `Get tile x from pixel x`() {
            testGetX(0, 100)
            testGetX(1, 130)
            testGetX(2, 160)
        }

        @Test
        fun `The first tile below startPixelX is -1`() {
            assertEquals(-1, tileRenderer.getX(99))
        }

        private fun testGetX(tile: Int, startPixelX: Int) {
            for (x: Int in startPixelX until startPixelX + 30) {
                assertEquals(tile, tileRenderer.getX(x))
            }
        }
    }

    @Nested
    inner class GetTileY {

        @Test
        fun `Get tile y from pixel y`() {
            testGetY(0, 200)
            testGetY(1, 240)
            testGetY(2, 280)
        }

        @Test
        fun `The first tile below startPixelY is -1`() {
            assertEquals(-1, tileRenderer.getY(199))
        }

        private fun testGetY(tile: Int, startPixelY: Int) {
            for (x: Int in startPixelY until startPixelY + 40) {
                assertEquals(tile, tileRenderer.getY(x))
            }
        }
    }

    @Nested
    inner class RenderTile {

        @Test
        fun `An empty tile renders nothing`() {
            tileRenderer.renderTile(EmptyTile, 1, 2, THREE)

            verify { renderer wasNot Called }
        }

        @Test
        fun `An empty tile renders nothing with default size`() {
            tileRenderer.renderTile(EmptyTile, 1, 2)

            verify { renderer wasNot Called }
        }

        @Test
        fun `A full tile renders a full tile`() {
            tileRenderer.renderTile(FullTile(RED), 5, 6, TWO)

            verifyFull(60, 80)
        }

        @Test
        fun `A full tile renders a full tile with default size`() {
            tileRenderer.renderTile(FullTile(RED), 5, 6)

            verifyFull(30, 40)
        }

        @Test
        fun `A unicode tile renders a unicode character`() {
            tileRenderer.renderTile(UnicodeTile('T'.code, GREEN), 2, 3, FOUR)

            verifyUnicode('T', 160, 220, 400)
        }

        @Test
        fun `A unicode tile renders a unicode  with default size`() {
            tileRenderer.renderTile(UnicodeTile('@'.code, GREEN), 2, 3)

            verifyUnicode('@', 40, 175, 340)
        }
    }

    @Nested
    inner class RenderFullTile {

        @Test
        fun `Render full tile with default size`() {
            tileRenderer.renderFullTile(RED, 5, 6)

            verifyFull(30, 40)
        }

        @Test
        fun `Render full tile`() {
            tileRenderer.renderFullTile(RED, 5, 6, THREE)

            verifyFull(90, 120)
        }
    }

    @Nested
    inner class RenderUnicodeTile {

        @Test
        fun `Render character with default size`() {
            tileRenderer.renderUnicodeTile('@', GREEN, 2, 3)

            verifyUnicode('@', 40, 175, 340)
        }

        @Test
        fun `Render character`() {
            tileRenderer.renderUnicodeTile('?', GREEN, 2, 3, FOUR)

            verifyUnicode('?', 160, 220, 400)
        }

        @Test
        fun `Render code point with default size`() {
            tileRenderer.renderUnicodeTile('@'.code, GREEN, 2, 3)

            verifyUnicode('@', 40, 175, 340)
        }

        @Test
        fun `Render code point`() {
            tileRenderer.renderUnicodeTile('?'.code, GREEN, 2, 3, FOUR)

            verifyUnicode('?', 160, 220, 400)
        }
    }

    @Nested
    inner class RenderText {

        @Test
        fun `Render text with default size`() {
            tileRenderer.renderText("Test", Color.BLUE, 0, 1)

            verify(40, 115, 145, 175, 205, 260)
        }

        @Test
        fun `Render text`() {
            tileRenderer.renderText("Test", Color.BLUE, 0, 1, TWO)

            verify(80, 130, 190, 250, 310, 280)
        }

        @Test
        fun `Render emoji`() {
            tileRenderer.renderText("🌳🚀", RED, 0, 0)

            verifySequence {
                renderer.setColor(RED)
                renderer.setFont(40)
                renderer.renderUnicode(127795, 115, 220)
                renderer.renderUnicode(128640, 145, 220)
            }
        }

        private fun verify(fontSize: Int, x0: Int, x1: Int, x2: Int, x3: Int, y: Int) {
            verifySequence {
                renderer.setColor(Color.BLUE)
                renderer.setFont(fontSize)
                renderer.renderUnicode('T'.code, x0, y)
                renderer.renderUnicode('e'.code, x1, y)
                renderer.renderUnicode('s'.code, x2, y)
                renderer.renderUnicode('t'.code, x3, y)
            }
        }
    }

    private fun verifyFull(width: Int, height: Int) {
        verifySequence {
            renderer.setColor(RED)
            renderer.renderRectangle(250, 440, width, height)
        }
    }

    private fun verifyUnicode(unicode: Char, fontSize: Int, x: Int, y: Int) {
        verifySequence {
            renderer.setColor(GREEN)
            renderer.setFont(fontSize)
            renderer.renderUnicode(unicode.code, x, y)
        }
    }

}