package ofws.core.render

import ofws.core.game.component.*
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.ecs.EcsState
import ofws.math.Rectangle
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size2d
import ofws.math.map.TileIndex

class GameRenderer(
    val area: Rectangle,
    val renderer: TileRenderer,
) {

    constructor(size: Size2d, renderer: TileRenderer) : this(Rectangle(size), renderer)

    fun renderEntities(state: EcsState) {
        for ((_, footprint, graphic) in state.query2<Footprint, Graphic>()) {
            renderBody(footprint, graphic)
        }
    }

    private fun renderBody(body: Footprint, graphic: Graphic) = when (body) {
        is SimpleFootprint -> renderTile(graphic.get(0), body.position)
        is BigFootprint -> renderTile(graphic.get(0), body.position, body.size)
        is SnakeFootprint -> for (pos in body.positions) {
            renderTile(graphic.get(0), pos)
        }
    }

    private fun renderTile(tile: Tile, index: TileIndex, bodySize: Size1d = ONE) =
        renderTile(renderer, tile, area.getParentX(index), area.getParentY(index), bodySize)

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