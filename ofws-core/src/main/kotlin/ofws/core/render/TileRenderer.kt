package ofws.core.render

import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size2d
import ofws.math.requireGreater

/**
 * A renderer using a grid of [Tiles][Tile].
 */
class TileRenderer(
    private val renderer: Renderer,
    private val startPixelX: Int,
    private val startPixelY: Int,
    private val tileSize: Size2d,
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
        size: Size1d = ONE
    ) {
        with(renderer) {
            setColor(color)
            setFont(size.size * tileSize.y)

            var centerX = getCenterPixelX(x, size)
            val centerY = getCenterPixelY(y, size)

            for (character in text.codePoints()) {
                renderUnicode(character, centerX, centerY)
                centerX += size.size * tileSize.x
            }
        }
    }

    fun renderTile(
        tile: Tile,
        x: Int,
        y: Int,
        size: Size1d = ONE
    ) {
        when (tile) {
            is FullTile -> renderFullTile(tile.color, x, y, size)
            is UnicodeTile -> renderUnicodeTile(tile.codePoint, tile.color, x, y, size)
            is EmptyTile -> return
        }
    }

    fun renderUnicodeTile(
        character: Char,
        color: Color,
        x: Int,
        y: Int,
        size: Size1d = ONE
    ) = renderUnicodeTile(character.code, color, x, y, size)

    fun renderUnicodeTile(
        codePoint: Int,
        color: Color,
        x: Int,
        y: Int,
        size: Size1d = ONE
    ) {
        with(renderer) {
            setColor(color)
            setFont(size.size * tileSize.y)
            renderUnicode(codePoint, getCenterPixelX(x, size), getCenterPixelY(y, size))
        }
    }

    fun renderFullTile(
        color: Color,
        x: Int,
        y: Int,
        size: Size1d = ONE
    ) {
        renderer.setColor(color)
        renderer.renderRectangle(getStartPixelX(x), getStartPixelY(y), tileSize.x * size.size, tileSize.y * size.size)
    }

    fun getX(pixelX: Int) = (pixelX - startPixelX) / tileSize.x + if (pixelX < startPixelX) -1 else 0

    fun getY(pixelY: Int) = (pixelY - startPixelY) / tileSize.y + if (pixelY < startPixelY) -1 else 0

    private fun getStartPixelX(x: Int) = startPixelX + x * tileSize.x

    private fun getStartPixelY(y: Int) = startPixelY + y * tileSize.y

    private fun getCenterPixelX(x: Int, size: Size1d) = getStartPixelX(x) + size.size * tileSize.x / 2

    private fun getCenterPixelY(y: Int, size: Size1d) = getStartPixelY(y) + size.size * tileSize.y / 2

}