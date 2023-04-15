package ofws.math

import ofws.math.map.Index

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

    /**
     * The number of tiles inside the rectangle defined by the [Size].
     */
    val tiles: Int
        get() = x * y

    /**
     * Returns the [Index] of [x] & [y], but the calculation is only valid inside the defined rectangle.
     * Use the slower [getIndexIfInside] otherwise.
     */
    fun getIndex(x: Int, y: Int) = Index(y * this.x + x)

    /**
     * Returns the [Index] of [x] & [y] if inside the defined rectangle or null.
     */
    fun getIndexIfInside(x: Int, y: Int) = if (isInside(x, y)) {
        getIndex(x, y)
    } else {
        null
    }

    fun getPoint(index: Index) = Pair(getX(index), getY(index))

    fun getX(index: Index) = index.index % x

    fun getY(index: Index) = index.index / x

    fun isInside(index: Index) = index.index in 0 until tiles

    fun isInside(x: Int, y: Int) = isInsideForX(x) && isInsideForY(y)

    fun isInsideForX(x: Int) = x in 0 until this.x

    fun isInsideForY(y: Int) = y in 0 until this.y

}