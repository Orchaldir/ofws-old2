package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size2d
import ofws.math.map.TileIndex

data class EntityMap(
    private val size: Size2d,
    private val entities: Map<TileIndex, Entity>
) {

    fun getEntity(x: Int, y: Int): Entity? {
        val index = size.getIndexIfInside(x, y) ?: return null
        return getEntity(index)
    }

    fun getEntity(index: TileIndex) = entities[index]

}