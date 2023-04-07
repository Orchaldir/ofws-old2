package ofws.app.rendering

import javafx.geometry.VPos
import javafx.scene.canvas.GraphicsContext
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import ofws.core.render.Color
import ofws.core.render.Renderer

class CanvasRenderer(
    private val graphicsContext: GraphicsContext
) : Renderer {

    init {
        graphicsContext.isImageSmoothing = false
    }

    override fun clear() {
        val canvas = graphicsContext.canvas
        graphicsContext.clearRect(0.0, 0.0, canvas.width, canvas.height)
    }

    override fun renderUnicode(text: String, centerX: Int, centerY: Int) =
        graphicsContext.fillText(text, centerX.toDouble(), centerY.toDouble())

    override fun renderRectangle(x: Int, y: Int, width: Int, height: Int) =
        graphicsContext.fillRect(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())

    override fun setColor(color: Color) {
        graphicsContext.fill =
            javafx.scene.paint.Color(color.getRedDouble(), color.getGreenDouble(), color.getBlueDouble(), 1.0)
    }

    override fun setFont(size: Int, name: String) = with(graphicsContext) {
        font = Font(name, size.toDouble())
        textAlign = TextAlignment.CENTER
        textBaseline = VPos.CENTER
    }
}