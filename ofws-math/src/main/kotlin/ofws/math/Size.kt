package ofws.math

import ofws.math.map.TileIndex

/**
 * A Size defines a 2d rectangle starting at the origin.
 */
data class Size(
    /**
     * The Size along the x-axis. Also known as width.
     */
    val x: Int,
    /**
     * The Size along the y-axis. Also known as height.
     */
    val y: Int
) {

    init {
        requireGreater(x, 1, "x")
        requireGreater(y, 1, "y")
    }

    /**
     * The number of tiles inside the rectangle defined by the [Size].
     */
    val tiles: Int
        get() = x * y

    /**
     * Returns the [TileIndex] of [x] & [y], but the calculation is only valid inside the defined rectangle.
     * Use the slower [getIndexIfInside] otherwise.
     */
    fun getIndex(x: Int, y: Int) = TileIndex(y * this.x + x)

    /**
     * Returns the [TileIndex] of [x] & [y] if inside the defined rectangle or null.
     */
    fun getIndexIfInside(x: Int, y: Int) = if (isInside(x, y)) {
        getIndex(x, y)
    } else {
        null
    }

    /**
     * Returns a list of [TileIndex] that are inside the map and the desired rectangle.
     */
    fun getIndices(index: TileIndex, size: Int): List<TileIndex> {
        if (!isInside(index, size)) return emptyList()

        val indices = mutableListOf<TileIndex>()

        for (dy in 0 until size) {
            var currentIndex = index.index + dy * x

            for (dx in 0 until size) {
                indices.add(TileIndex(currentIndex++))
            }
        }

        return indices
    }

    fun getPoint(index: TileIndex) = Pair(getX(index), getY(index))

    fun getX(index: TileIndex) = index.index % x

    fun getY(index: TileIndex) = index.index / x

    fun isInside(index: TileIndex) = index.index in 0 until tiles

    fun isInside(index: TileIndex, size: Int): Boolean {
        val (startX, startY) = getPoint(index)
        val endX = startX + size - 1
        val endY = startY + size - 1

        return isInside(startX, startY) && isInside(endX, endY)
    }

    fun isInside(x: Int, y: Int) = isInsideForX(x) && isInsideForY(y)

    fun isInsideForX(x: Int) = x in 0 until this.x

    fun isInsideForY(y: Int) = y in 0 until this.y

}