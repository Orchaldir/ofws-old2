package ofws.core.render

import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.math.Rectangle
import ofws.math.Size2d
import ofws.math.map.TileIndex

class GameRenderer(
    val area: Rectangle,
    val renderer: TileRenderer,
) {

    constructor(size: Size2d, renderer: TileRenderer) : this(Rectangle(size), renderer)

    fun renderMap(map: GameMap, getTile: (tile: Terrain) -> Tile) {
        for (y in 0 until area.size.y) {
            for (x in 0 until area.size.x) {
                val mapIndex = area.size.getIndex(x, y)

                renderTile(map, getTile, mapIndex, x, y)
            }
        }
    }

    fun renderTiles(
        map: GameMap,
        getTile: (tile: Terrain) -> Tile,
        indices: Set<TileIndex>
    ) {
        for (index in indices) {
            val (x, y) = area.size.getPoint(index)

            renderTile(map, getTile, index, x, y)
        }
    }

    private fun renderTile(
        map: GameMap,
        getTile: (tile: Terrain) -> Tile,
        index: TileIndex,
        x: Int,
        y: Int
    ) {
        if (map.entityMap.hasEntity(index)) {
            return
        }

        val terrain = map.tilemap.getTile(index)
        val tile = getTile(terrain)

        renderTile(renderer, tile, x, y)
    }

}