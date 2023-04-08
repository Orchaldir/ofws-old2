package ofws.core.render

import ofws.core.requireGreater

class TileRenderer(
    private val renderer: Renderer,
    startPixelX: Int,
    startPixelY: Int,
    tileWidth: Int,
    tileHeight: Int
) {
    private val startPixelX = requireGreater(startPixelX, -1, "pixelX")
    private val startPixelY = requireGreater(startPixelY, -1, "pixelY")
    private val tileWidth = requireGreater(tileWidth, 0, "tileWidth")
    private val tileHeight = requireGreater(tileHeight, 0, "tileHeight")

    fun renderText(
        text: String,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        with(renderer) {
            setColor(color)
            setFont(size * tileHeight)

            var centerX = getCenterPixelX(x, size)
            val centerY = getCenterPixelY(y, size)

            val splitByCodePoint = splitByCodePoint(text)

            for (character in splitByCodePoint) {
                renderUnicode(character, centerX, centerY)
                centerX += tileWidth * size
            }
        }
    }

    fun renderUnicodeTile(
        text: String,
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        with(renderer) {
            setColor(color)
            setFont(size * tileHeight)
            renderUnicode(text, getCenterPixelX(x, size), getCenterPixelY(y, size))
        }
    }

    fun renderFullTile(
        color: Color,
        x: Int,
        y: Int,
        size: Int = 1
    ) {
        renderer.setColor(color)
        renderer.renderRectangle(getStartPixelX(x), getStartPixelY(y), tileWidth * size, tileHeight * size)
    }

    fun getX(pixelX: Int) = (pixelX - startPixelX) / tileWidth + if (pixelX < startPixelX) -1 else 0

    fun getY(pixelY: Int) = (pixelY - startPixelY) / tileHeight + if (pixelY < startPixelY) -1 else 0

    private fun getStartPixelX(x: Int) = startPixelX + x * tileWidth

    private fun getStartPixelY(y: Int) = startPixelY + y * tileHeight

    private fun getCenterPixelX(x: Int, size: Int) = getStartPixelX(x) + size * tileWidth / 2

    private fun getCenterPixelY(y: Int, size: Int) = getStartPixelY(y) + size * tileHeight / 2

    private fun splitByCodePoint(str: String): Array<String> {
        val codePoints = str.codePoints().toArray()
        return Array(codePoints.size) { index ->
            String(codePoints, index, 1)
        }
    }
}