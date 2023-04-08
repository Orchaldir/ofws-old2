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

            verifySequence {
                renderer.setColor(Color.RED)
                renderer.renderRectangle(250, 440, 30, 40)
            }
        }

        @Test
        fun `Render full tile with size`() {
            tileRenderer.renderFullTile(Color.RED, 5, 6, 3)

            verifySequence {
                renderer.setColor(Color.RED)
                renderer.renderRectangle(250, 440, 90, 120)
            }
        }
    }

    @Nested
    inner class RenderUnicodeTile {

        @Test
        fun `Render unicode tile with default size`() {
            tileRenderer.renderUnicodeTile("@", Color.GREEN, 2, 3)

            verifySequence {
                renderer.setColor(Color.GREEN)
                renderer.setFont(40)
                renderer.renderUnicode("@",  175, 340)
            }
        }

        @Test
        fun `Render unicode tile with size`() {
            tileRenderer.renderUnicodeTile("?", Color.GREEN, 2, 3, 4)

            verifySequence {
                renderer.setColor(Color.GREEN)
                renderer.setFont(160)
                renderer.renderUnicode("?",  220, 400)
            }
        }
    }

    @Nested
    inner class RenderText {

        @Test
        fun `Render text with default size`() {
            tileRenderer.renderText("Test", Color.BLUE, 0, 1)

            verifySequence {
                renderer.setColor(Color.BLUE)
                renderer.setFont(40)
                renderer.renderUnicode("T",  115, 260)
                renderer.renderUnicode("e",  145, 260)
                renderer.renderUnicode("s",  175, 260)
                renderer.renderUnicode("t",  205, 260)
            }
        }

        @Test
        fun `Render unicode tile with size`() {
            tileRenderer.renderText("Test", Color.BLUE, 0, 1, 2)

            verifySequence {
                renderer.setColor(Color.BLUE)
                renderer.setFont(80)
                renderer.renderUnicode("T",  130, 280)
                renderer.renderUnicode("e",  190, 280)
                renderer.renderUnicode("s",  250, 280)
                renderer.renderUnicode("t",  310, 280)
            }
        }
    }

}