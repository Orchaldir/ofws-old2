package ofws.core.render

import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TileRendererTest {

    val renderer = mockk<Renderer>(relaxed = true)
    val tileRenderer = TileRenderer(renderer, 100, 200, 30, 40)

    @Nested
    inner class RenderFullTile {

        @Test
        fun `Render full tile with default size`() {
            tileRenderer.renderFullTile(Color.RED, 5, 6)

            verify(30, 40)
        }

        @Test
        fun `Render full tile with size`() {
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
        fun `Render unicode tile with default size`() {
            tileRenderer.renderUnicodeTile("@", Color.GREEN, 2, 3)

            verify("@", 40, 175, 340)
        }

        @Test
        fun `Render unicode tile with size`() {
            tileRenderer.renderUnicodeTile("?", Color.GREEN, 2, 3, 4)

            verify("?", 160, 220, 400)
        }

        private fun verify(unicode: String, fontSize: Int, x: Int, y: Int) {
            verifySequence {
                renderer.setColor(Color.GREEN)
                renderer.setFont(fontSize)
                renderer.renderUnicode(unicode, x, y)
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
        fun `Render unicode tile with size`() {
            tileRenderer.renderText("Test", Color.BLUE, 0, 1, 2)

            verify(80, 130, 190, 250, 310, 280)
        }

        private fun verify(fontSize: Int, x0: Int, x1: Int, x2: Int, x3: Int, y: Int) {
            verifySequence {
                renderer.setColor(Color.BLUE)
                renderer.setFont(fontSize)
                renderer.renderUnicode("T", x0, y)
                renderer.renderUnicode("e", x1, y)
                renderer.renderUnicode("s", x2, y)
                renderer.renderUnicode("t", x3, y)
            }
        }
    }

}