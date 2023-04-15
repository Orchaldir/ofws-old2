package ofws.core.render

import io.mockk.mockk
import io.mockk.verifySequence
import ofws.math.Size
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TileRendererTest {

    val renderer = mockk<Renderer>(relaxed = true)
    val tileRenderer = TileRenderer(renderer, 100, 200, Size(30, 40))

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
    inner class RenderFullTile {

        @Test
        fun `Render full tile with default size`() {
            tileRenderer.renderFullTile(Color.RED, 5, 6)

            verify(30, 40)
        }

        @Test
        fun `Render full tile`() {
            tileRenderer.renderFullTile(Color.RED, 5, 6, 3)

            verify(90, 120)
        }

        private fun verify(width: Int, height: Int) {
            verifySequence {
                renderer.setColor(Color.RED)
                renderer.renderRectangle(250, 440, width, height)
            }
        }
    }

    @Nested
    inner class RenderUnicodeTile {

        @Test
        fun `Render character with default size`() {
            tileRenderer.renderUnicodeTile('@', Color.GREEN, 2, 3)

            verify('@', 40, 175, 340)
        }

        @Test
        fun `Render character`() {
            tileRenderer.renderUnicodeTile('?', Color.GREEN, 2, 3, 4)

            verify('?', 160, 220, 400)
        }

        @Test
        fun `Render code point with default size`() {
            tileRenderer.renderUnicodeTile('@'.code, Color.GREEN, 2, 3)

            verify('@', 40, 175, 340)
        }

        @Test
        fun `Render code point`() {
            tileRenderer.renderUnicodeTile('?'.code, Color.GREEN, 2, 3, 4)

            verify('?', 160, 220, 400)
        }

        private fun verify(unicode: Char, fontSize: Int, x: Int, y: Int) {
            verifySequence {
                renderer.setColor(Color.GREEN)
                renderer.setFont(fontSize)
                renderer.renderUnicode(unicode.code, x, y)
            }
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
            tileRenderer.renderText("Test", Color.BLUE, 0, 1, 2)

            verify(80, 130, 190, 250, 310, 280)
        }

        @Test
        fun `Render emoji`() {
            tileRenderer.renderText("🌳🚀", Color.RED, 0, 0)

            verifySequence {
                renderer.setColor(Color.RED)
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

}