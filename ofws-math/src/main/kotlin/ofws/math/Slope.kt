package ofws.math

/**
 * A line going through the origin and the point defined by [x] & [y].
 */
data class Slope(val x: Int, val y: Int) {

    /**
     * Returns the y of tile with the highest y-coordinate that has at least 1 corner below the [Slope] and the x-coordinate [localX].
     */
    fun calculateTopX(localX: Int) = ((localX * 2 + 1) * y + x - 1) / (x * 2)

    /**
     * Returns the y of tile with the lowest y-coordinate that has at least 1 corner above the [Slope] and the x-coordinate [localX].
     */
    fun calculateBottomX(localX: Int) = ((localX * 2 - 1) * y + x) / (x * 2)

}

/**
 * Creates a [Slope] through the top-left corner of the tile at [x] & [y].
 */
fun createSlopeThroughTopLeft(x: Int, y: Int) = Slope(x * 2 - 1, y * 2 + 1)

/**
 * Creates a [Slope] through the top-right corner of the tile at [x] & [y].
 */
fun createSlopeThroughTopRight(x: Int, y: Int) = Slope(x * 2 + 1, y * 2 + 1)