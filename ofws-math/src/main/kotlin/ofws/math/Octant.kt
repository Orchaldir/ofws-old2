package ofws.math

// Name is always the start of the octant. So NORTH is from north to northeast.
enum class Octant {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    fun getGlobal(originX: Int, originY: Int, x: Int, y: Int) = when (this) {
        NORTH -> Pair(originX + y, originY + x)
        NORTH_EAST -> Pair(originX + x, originY + y)
        EAST -> Pair(originX + x, originY - y)
        SOUTH_EAST -> Pair(originX + y, originY - x)
        SOUTH -> Pair(originX - y, originY - x)
        SOUTH_WEST -> Pair(originX - x, originY - y)
        WEST -> Pair(originX - x, originY + y)
        NORTH_WEST -> Pair(originX - y, originY + x)
    }
}
