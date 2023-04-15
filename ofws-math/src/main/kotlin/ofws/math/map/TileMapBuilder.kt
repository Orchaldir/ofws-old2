package ofws.math.map

import ofws.math.Size


data class TileMapBuilder<T>(
    val size: Size,
    private val tiles: MutableList<T>,
) {
    constructor(size: Size, tile: T) :
            this(size, MutableList(size.tiles) { tile })

    constructor(x: Int, y: Int, tile: T) :
            this(Size(x, y), tile)

    fun build() = TileMap(size, tiles)

    // tile

    fun addBorder(tile: T) = addRectangle(0, 0, size.x, size.y, tile)

    fun addRectangle(startX: Int, startY: Int, sizeX: Int, sizeY: Int, tile: T): TileMapBuilder<T> {
        val endX = startX + sizeX
        val endY = startY + sizeY

        for (x in startX until endX) {
            setTile(x, startY, tile)
            setTile(x, endY - 1, tile)
        }

        for (y in startY until endY) {
            setTile(startX, y, tile)
            setTile(endX - 1, y, tile)
        }

        return this
    }

    fun setTile(x: Int, y: Int, tile: T) = setTile(size.getIndex(x, y), tile)

    fun setTile(index: TileIndex, tile: T): TileMapBuilder<T> {
        tiles[index.index] = tile
        return this
    }
}