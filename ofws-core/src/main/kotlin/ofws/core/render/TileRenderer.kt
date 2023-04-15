package ofws.core.render

import ofws.core.requireGreater
import ofws.math.Size

class TileRenderer(
    private val renderer: Renderer,
    startPixelX: Int,
    startPixelY: Int,
    tileWidth: Int,
    tileHeight: Int
) {
    private val startPixelX = requireGreater(startPixelX, -1, "pixelX")
    private val startPixelY = requireGreater(startPixelY, -1, "pixelY")
    private val tileSize = Size(tileWidth, tileHeight)

    fun renderText(
        text: String,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        val sizeInPixel = size * tileSize.y

        with(renderer) {
            setColor(color)
            setFont(sizeInPixel)

            var centerX = getCenterPixelX(x, size)
            val centerY = getCenterPixelY(y, size)

            for (character in text.codePoints()) {
                renderUnicode(character, centerX, centerY)
                centerX += sizeInPixel
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