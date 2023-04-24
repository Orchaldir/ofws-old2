package ofws.core.render

import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import ofws.core.log.MessageLog
import ofws.core.log.inform
import ofws.core.log.warn
import ofws.core.render.Color.Companion.WHITE
import ofws.core.render.Color.Companion.YELLOW
import ofws.math.Rectangle
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size2d
import org.junit.jupiter.api.Test

class MessageLogRendererTest {

    private val tileRenderer = mockk<TileRenderer>(relaxed = true)
    private val renderer = MessageLogRenderer(Rectangle(10, 20, Size2d(15, 5)), tileRenderer)

    @Test
    fun `Render no messages`() {
        renderer.render(MessageLog())

        verify { tileRenderer wasNot Called }
    }

    @Test
    fun `Render info with default color`() {
        renderer.render(MessageLog(inform("Info test")))

        verifySequence {
            tileRenderer.renderText("1: Info test", WHITE, 10, 20, ONE)
        }
    }

    @Test
    fun `Render warning with default color`() {
        renderer.render(MessageLog(warn("Warning test")))

        verifySequence {
            tileRenderer.renderText("1: Warning test", YELLOW, 10, 20, ONE)
        }
    }

    @Test
    fun `Multiple messages, but less than the area can contain`() {
        renderer.render(MessageLog(listOf(inform("abc"), warn("def"))))

        verifySequence {
            tileRenderer.renderText("2: def", YELLOW, 10, 20, ONE)
            tileRenderer.renderText("1: abc", WHITE, 10, 21, ONE)
        }
    }

    @Test
    fun `More messages than the area can contain`() {
        renderer.render(MessageLog((0..20).map { warn("Test $it") }))

        verifySequence {
            tileRenderer.renderText("21: Test 20", YELLOW, 10, 20, ONE)
            tileRenderer.renderText("20: Test 19", YELLOW, 10, 21, ONE)
            tileRenderer.renderText("19: Test 18", YELLOW, 10, 22, ONE)
            tileRenderer.renderText("18: Test 17", YELLOW, 10, 23, ONE)
            tileRenderer.renderText("17: Test 16", YELLOW, 10, 24, ONE)
        }
    }

}