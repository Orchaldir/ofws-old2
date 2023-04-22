package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size1d
import ofws.math.Size2d
import ofws.math.map.TileIndex

data class EntityMapBuilder(
    private val size: Size2d,
    private val entities: MutableMap<TileIndex, Entity> = mutableMapOf(),
) {

    fun build() = EntityMap(size, entities)

    fun removeEntity(index: TileIndex, entity: Entity): EntityMapBuilder {
        val removed = entities.remove(index)

        if (removed != entity) {
            throw IllegalArgumentException("Removed entity $removed instead of $entity at index $index!")
        }

        return this
    }

    fun removeEntity(index: TileIndex, entity: Entity, size: Size1d): EntityMapBuilder {
        val indices = this.size.getIndices(index, size)

        if (indices.isEmpty()) {
            throw IllegalArgumentException("Can not remove entity $entity at index $index with size $size!")
        }

        indices.forEach { i -> removeEntity(i, entity) }

        return this
    }

    fun setEntity(index: TileIndex, entity: Entity): EntityMapBuilder {
        val overwritten = entities.put(index, entity)

        if (overwritten != null && overwritten != entity) {
            throw IllegalArgumentException("Overwritten entity $overwritten with $entity at index $index!")
        }

        return this
    }

    fun setEntity(index: TileIndex, entity: Entity, size: Size1d): EntityMapBuilder {
        val indices = this.size.getIndices(index, size)

        if (indices.isEmpty()) {
            throw IllegalArgumentException("Can not set entity $entity at index $index with size $size!")
        }

        indices.forEach { i -> setEntity(i, entity) }

        return this
    }

}