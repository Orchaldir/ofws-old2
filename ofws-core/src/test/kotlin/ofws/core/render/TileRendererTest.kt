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

}