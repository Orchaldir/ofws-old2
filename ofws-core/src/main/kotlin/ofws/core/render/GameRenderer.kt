package ofws.core.render

import mu.KotlinLogging
import ofws.core.game.component.*
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.ecs.EcsState
import ofws.math.Rectangle
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.pathfinding.graph.OccupancyMap
import kotlin.math.max
import kotlin.math.min

private val logger = KotlinLogging.logger {}

data class GameRenderer(
    val area: Rectangle,
    val renderer: TileRenderer,
) {

    constructor(size: Size2d, renderer: TileRenderer) : this(Rectangle(size), renderer)

    /**
     * Renders an integer for each tile.
     * It maps the range between the min & max value to a factor between 0 & 1.
     */
    fun renderInts(size: Size2d, values: List<Int?>, getTile: (factor: Double) -> Tile) {
        if (size.tiles != values.size) {
            return
        }

        var maxValue = Int.MIN_VALUE
        var minValue = Int.MAX_VALUE

        values.forEach {
            if (it != null) {
                maxValue = max(maxValue, it)
                minValue = min(minValue, it)
            }
        }

        val diff = (maxValue - minValue).toDouble()

        logger.info("renderCosts(): min=$minValue max=$maxValue")

        for (y in 0 until area.size.y) {
            for (x in 0 until area.size.x) {
                val mapIndex = size.getIndexIfInside(x, y) ?: continue
                val value = values[mapIndex.index] ?: continue
                val factor = (value - minValue) / diff
                val tile = getTile(factor)

                renderer.renderTile(tile, area.startX + x, area.startY + y)
            }
        }
    }

    /**
     * Renders all entities with a [Footprint] & [Graphic] component.
     */
    fun renderEntities(state: EcsState, mapSize2d: Size2d) {
        for ((_, footprint, graphic) in state.query2<Footprint, Graphic>()) {
            renderEntity(mapSize2d, footprint, graphic)
        }
    }

    private fun renderEntity(mapSize2d: Size2d, body: Footprint, graphic: Graphic) = when (body) {
        is SimpleFootprint -> renderEntityTile(mapSize2d, graphic.get(0), body.position)
        is BigFootprint -> renderEntityTile(mapSize2d, graphic.get(0), body.position, body.size)
        is SnakeFootprint -> for (pos in body.positions) {
            renderEntityTile(mapSize2d, graphic.get(0), pos)
        }
    }

    private fun renderEntityTile(mapSize2d: Size2d, tile: Tile, index: TileIndex, bodySize: Size1d = ONE) {
        val (x, y) = mapSize2d.getPoint(index)

        if (area.size.isInside(x, y)) {
            renderer.renderTile(tile, area.startX + x, area.startY + y, bodySize)
        }
    }

    /**
     * Renders a whole map.
     */
    fun renderMap(map: GameMap, getTile: (terrain: Terrain) -> Tile) {
        for (y in 0 until area.size.y) {
            for (x in 0 until area.size.x) {
                val mapIndex = map.getSize().getIndexIfInside(x, y)

                if (mapIndex != null) {
                    renderTile(map, getTile, mapIndex, x, y)
                }
            }
        }
    }

    /**
     * Renders if an entity can occupy a tile or not.
     */
    fun renderOccupancyMap(map: OccupancyMap) {
        for (y in 0 until area.size.y) {
            for (x in 0 until area.size.x) {
                val mapIndex = map.getSize().getIndexIfInside(x, y)

                if (mapIndex != null) {
                    val color = if (map.isValid(mapIndex)) Color.GREEN else Color.RED

                    renderer.renderFullTile(color, area.startX + x, area.startY + y)
                }
            }
        }
    }

    /**
     * Renders part of a map.
     */
    fun renderTiles(
        map: GameMap,
        indices: Set<TileIndex>,
        getTile: (tile: Terrain) -> Tile,
    ) {
        for (index in indices) {
            val (x, y) = map.getSize().getPoint(index)

            if (area.size.isInside(x, y)) {
                renderTile(map, getTile, index, x, y)
            }
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

        renderer.renderTile(tile, area.startX + x, area.startY + y)
    }

}