package ofws.core.game.map

import ofws.core.game.component.BigFootprint
import ofws.core.game.component.Footprint
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.SnakeFootprint
import ofws.ecs.Entity
import ofws.math.Size1d
import ofws.math.Size2d
import ofws.math.map.TileIndex

data class EntityMapBuilder(
    private val size: Size2d,
    private val entities: MutableMap<TileIndex, Entity> = mutableMapOf(),
) {

    fun addEntity(index: TileIndex, entity: Entity): EntityMapBuilder {
        if (!size.isInside(index)) {
            throw IllegalArgumentException("Can not add $entity at $index, which is outside the map!")
        }

        val overwritten = entities.put(index, entity)

        if (overwritten != null && overwritten != entity) {
            throw IllegalArgumentException("Overwritten $overwritten with $entity at $index!")
        }

        return this
    }

    fun addEntity(index: TileIndex, entity: Entity, size: Size1d): EntityMapBuilder {
        if (!this.size.isInside(index, size)) {
            throw IllegalArgumentException("Can not add $entity at $index with $size, which is outside the map!")
        }

        this.size.getIndices(index, size).forEach { i -> addEntity(i, entity) }

        return this
    }

    fun addFootprint(entity: Entity, footprint: Footprint) = when (footprint) {
        is SimpleFootprint -> addEntity(footprint.position, entity)

        is BigFootprint -> addEntity(footprint.position, entity, footprint.size)

        is SnakeFootprint -> {
            footprint.positions.forEach { p -> addEntity(p, entity) }
            this
        }
    }

    fun build() = EntityMap(size, entities)

    fun removeEntity(index: TileIndex, entity: Entity): EntityMapBuilder {
        val removed = entities.remove(index)

        if (removed == null) {
            throw IllegalArgumentException("$entity was not at $index to remove!")
        } else if (removed != entity) {
            throw IllegalArgumentException("Removed $removed instead of $entity at $index!")
        }

        return this
    }

    fun removeEntity(index: TileIndex, entity: Entity, size: Size1d): EntityMapBuilder {
        this.size.getIndices(index, size).forEach { i -> removeEntity(i, entity) }

        return this
    }

    fun removeFootprint(entity: Entity, footprint: Footprint) = when (footprint) {
        is SimpleFootprint -> removeEntity(footprint.position, entity)

        is BigFootprint -> removeEntity(footprint.position, entity, footprint.size)

        is SnakeFootprint -> {
            footprint.positions.forEach { p -> removeEntity(p, entity) }
            this
        }
    }

}