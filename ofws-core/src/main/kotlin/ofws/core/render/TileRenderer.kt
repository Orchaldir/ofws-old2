package ofws.core.render

import ofws.math.Size
import ofws.math.requireGreater

class TileRenderer(
    private val renderer: Renderer,
    private val startPixelX: Int,
    private val startPixelY: Int,
    private val tileSize: Size,
) {

    init {
        requireGreater(startPixelX, 0, "startPixelX")
        requireGreater(startPixelY, 0, "startPixelY")
    }

    fun renderText(
        text: String,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        with(renderer) {
            setColor(color)
            setFont(size * tileSize.y)

            var centerX = getCenterPixelX(x, size)
            val centerY = getCenterPixelY(y, size)

            for (character in text.codePoints()) {
                renderUnicode(character, centerX, centerY)
                centerX += size * tileSize.x
            }
        }
    }

    fun renderUnicodeTile(
        character: Char,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) = renderUnicodeTile(character.code, color, x, y, size)

    fun renderUnicodeTile(
        codePoint: Int,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        with(renderer) {
            setColor(color)
            setFont(size * tileSize.y)
            renderUnicode(codePoint, getCenterPixelX(x, size), getCenterPixelY(y, size))
        }
    }

    fun renderFullTile(
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        renderer.setColor(color)
        renderer.renderRectangle(getStartPixelX(x), getStartPixelY(y), tileSize.x * size, tileSize.y * size)
    }

    fun getX(pixelX: Int) = (pixelX - startPixelX) / tileSize.x + if (pixelX < startPixelX) -1 else 0

    fun getY(pixelY: Int) = (pixelY - startPixelY) / tileSize.y + if (pixelY < startPixelY) -1 else 0

    private fun getStartPixelX(x: Int) = startPixelX + x * tileSize.x

    private fun getStartPixelY(y: Int) = startPixelY + y * tileSize.y

    private fun getCenterPixelX(x: Int, size: Int) = getStartPixelX(x) + size * tileSize.x / 2

    private fun getCenterPixelY(y: Int, size: Int) = getStartPixelY(y) + size * tileSize.y / 2

}