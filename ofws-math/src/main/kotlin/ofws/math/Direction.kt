package ofws.math

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    /**
     * Does the offset point to one of the 4 neighbors?
     */
    fun isNeighbor(offsetX: Int, offsetY: Int) = offsetX == this.x && offsetY == this.y
}

/**
 * Returns a direction, if the offset points to one of the 4 neighbors.
 */
fun getDirection(offsetX: Int, offsetY: Int) = Direction.values().firstOrNull { d -> d.isNeighbor(offsetX, offsetY) }