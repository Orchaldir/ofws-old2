package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size2d
import ofws.math.map.TileIndex

/**
 * Stores which entity occupies each tile.
 */
data class EntityMap(
    private val size: Size2d,
    private val entities: Map<TileIndex, Entity> = mapOf()
) {

    fun getEntity(x: Int, y: Int): Entity? {
        val index = size.getIndexIfInside(x, y) ?: return null
        return getEntity(index)
    }

    fun getEntity(index: TileIndex) = entities[index]

    fun hasEntity(x: Int, y: Int): Boolean {
        val index = size.getIndexIfInside(x, y) ?: return false
        return hasEntity(index)
    }

    fun hasEntity(index: TileIndex) = entities.containsKey(index)

    fun builder() = EntityMapBuilder(size, entities.toMutableMap())

}