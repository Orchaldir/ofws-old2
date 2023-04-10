package ofws.math.map

import ofws.math.Size


class TileMapBuilder<T>(
    val size: Size,
    private val terrainList: MutableList<T>,
) {
    constructor(size: Size, tile: T) :
            this(size, MutableList(size.tiles) { tile })

    constructor(x: Int, y: Int, terrain: T) :
            this(Size(x, y), terrain)

    fun build() = TileMap(size, terrainList)

    // terrain

    fun addBorder(terrain: T) = addRectangle(0, 0, size.x, size.y, terrain)

    fun addRectangle(startX: Int, startY: Int, sizeX: Int, sizeY: Int, terrain: T): TileMapBuilder<T> {
        val endX = startX + sizeX
        val endY = startY + sizeY

        for (x in startX until endX) {
            terrainList[size.getPosition(x, startY).index] = terrain
            terrainList[size.getPosition(x, endY - 1).index] = terrain
        }

        for (y in startY until endY) {
            terrainList[size.getPosition(startX, y).index] = terrain
            terrainList[size.getPosition(endX - 1, y).index] = terrain
        }

        return this
    }

    fun getTerrainList() = terrainList

    fun getTerrain(x: Int, y: Int) = terrainList[size.getPosition(x, y).index]

    fun getTerrain(index: Int): T {
        return terrainList[index]
    }

    fun setTerrain(x: Int, y: Int, terrain: T): TileMapBuilder<T> {
        terrainList[size.getPosition(x, y).index] = terrain
        return this
    }

    fun setTerrain(index: Int, terrain: T): TileMapBuilder<T> {
        terrainList[index] = terrain
        return this
    }
}