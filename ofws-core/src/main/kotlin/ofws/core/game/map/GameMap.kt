package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size1d
import ofws.math.map.TileIndex
import ofws.math.map.TileMap
import ofws.math.pathfinding.graph.OccupancyMap

data class GameMap(
    val tilemap: TileMap<Terrain>,
    val entityMap: EntityMap,
) {

    constructor(tilemap: TileMap<Terrain>) : this(tilemap, EntityMap(tilemap.size))

    fun checkWalkability(position: TileIndex, entity: Entity): Walkability {
        if (!tilemap.size.isInside(position)) {
            return OutsideMap
        } else if (!tilemap.getTile(position).isWalkable()) {
            return BlockedByObstacle
        }

        return when (val blockingEntity = entityMap.getEntity(position)) {
            null, entity -> Walkable(position)
            else -> BlockedByEntity(blockingEntity)
        }
    }

    fun checkWalkability(position: TileIndex, entity: Entity, size: Size1d): Walkability {
        val indices = this.tilemap.size.getIndices(position, size)

        if (indices.isEmpty()) {
            return OutsideMap
        }

        indices.forEach { i ->
            val result = checkWalkability(i, entity)
            if (result !is Walkable) {
                return result
            }
        }

        return Walkable(position)
    }

    fun createOccupancyMap(entity: Entity): OccupancyMap {
        val list = getSize()
            .getIndices()
            .map { checkWalkability(it, entity) is Walkable }

        return OccupancyMap(list, getSize())
    }

    fun createOccupancyMap(entity: Entity, entitySize: Size1d): OccupancyMap {
        val list = getSize()
            .getIndices()
            .map { checkWalkability(it, entity, entitySize) is Walkable }

        return OccupancyMap(list, getSize())
    }

    fun getSize() = tilemap.size

}