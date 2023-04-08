package ofws.core.render

import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TilesTest {
    val renderer = mockk<TileRenderer>(relaxed = true)

    @Nested
    inner class RenderTile {

        @Test
        fun `An empty tile renders nothing`() {
            renderTile(renderer, EmptyTile, 1, 2, 3)

            verify { renderer wasNot Called }
        }

        @Test
        fun `An empty tile renders nothing with default size`() {
            renderTile(renderer, EmptyTile, 1, 2)

            verify { renderer wasNot Called }
        }

        @Test
        fun `A full tile renders a full tile`() {
            renderTile(renderer, FullTile(Color.WHITE), 10, 20, 30)

            verify {
                renderer.renderFullTile(Color.WHITE, 10, 20, 30)
            }
        }

        @Test
        fun `A full tile renders a full tile with default size`() {
            renderTile(renderer, FullTile(Color.BLUE), 10, 20)

            verify {
                renderer.renderFullTile(Color.BLUE, 10, 20, 1)
            }
        }

        @Test
        fun `A unicode tile renders a unicode character`() {
            renderTile(renderer, UnicodeTile('T'.code, Color.GREEN), 5, 6, 7)

            verify {
                renderer.renderUnicodeTile('T'.code, Color.GREEN, 5, 6, 7)
            }
        }

        @Test
        fun `A unicode tile renders a unicode  with default size`() {
            renderTile(renderer, UnicodeTile('@'.code, Color.RED), 3, 2)

            verify {
                renderer.renderUnicodeTile('@'.code, Color.RED, 3, 2, 1)
            }
        }
    }
}