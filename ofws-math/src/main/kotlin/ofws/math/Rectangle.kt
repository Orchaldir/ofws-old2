package ofws.math

import ofws.math.map.TileIndex

data class Rectangle(
    val startX: Int,
    val startY: Int,
    val size: Size2d
) {

    /**
     * Converts from the parent coordinate frame to the rectangle's coordinate frame.
     */
    fun convertToInside(parentX: Int, parentY: Int) = size.getIndexIfInside(parentX - startX, parentY - startY)

    /**
     * Returns the x coordinate in the parent coordinate frame for a tile of the rectangle.
     */
    fun getParentX(index: TileIndex) = startX + size.getX(index)

    /**
     * Returns the y coordinate in the parent coordinate frame for a tile of the rectangle.
     */
    fun getParentY(index: TileIndex) = startY + size.getY(index)

    // inside check

    fun isInside(parentX: Int, parentY: Int, size: Size1d) =
        isInside(parentX, parentY) && isInside(parentX + size.size, parentY + size.size)

    fun isInside(parentX: Int, parentY: Int) = isInsideForX(parentX) && isInsideForY(parentY)

    fun isInsideForX(parentX: Int) = size.isInsideForX(parentX - startX)

    fun isInsideForY(parentY: Int) = size.isInsideForY(parentY - startY)
}