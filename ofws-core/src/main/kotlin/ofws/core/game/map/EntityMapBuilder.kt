package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.Size
import ofws.math.map.TileIndex

data class EntityMapBuilder(
    private val size: Size,
    private val entities: MutableMap<TileIndex, Entity> = mutableMapOf(),
) {

    fun build() = EntityMap(size, entities)

    fun setEntity(index: TileIndex, entity: Entity): EntityMapBuilder {
        val overwritten = entities.put(index, entity)

        if (overwritten != null && overwritten != entity) {
            throw IllegalArgumentException("Overwritten entity $overwritten with $entity at index $index!")
        }

        return this
    }

    fun setEntity(index: TileIndex, entity: Entity, size: Int): EntityMapBuilder {
        val indices = this.size.getIndices(index, size)

        if (indices.isEmpty()) {
            throw IllegalArgumentException("Can not set entity $entity at index $index with size $size!")
        }

        indices.forEach { i -> setEntity(i, entity) }

        return this
    }

}