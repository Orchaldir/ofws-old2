package ofws.core.game.reducer

import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.getSize
import ofws.core.game.component.updateFootprint
import ofws.core.game.map.EntityMap
import ofws.core.game.map.GameMap
import ofws.core.game.map.Walkable
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.map.TileIndex
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val UPDATE_POSITION_REDUCER: Reducer<UpdatePosition, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val storage = state.getStorage<Footprint>()!!
    val footprint = storage.getOrThrow(action.entity)
    val walkability = checkPosition(map, action.entity, footprint, action.index)

    val newState = when (walkability) {
        is Walkable -> {
            val newFootprint = updateFootprint(footprint, action.index, action.direction)
            val newMap = updateMap(map, action.entity, footprint, newFootprint)
            val newFootprintStorage = storage.updateAndRemove(mapOf(action.entity to newFootprint))
            state.copy(listOf(newFootprintStorage), listOf(newMap))
        }

        else -> handleError(state, walkability)
    }

    noFollowUps(newState)
}

fun checkPosition(map: GameMap, entity: Entity, footprint: Footprint, position: TileIndex) = map.checkWalkability(
    position,
    entity,
    getSize(footprint),
)

fun updateMap(map: GameMap, entity: Entity, old: Footprint, new: Footprint) = map
    .copy(entityMap = updateMap(map.entityMap, entity, old, new))

fun updateMap(map: EntityMap, entity: Entity, old: Footprint, new: Footprint) = map.builder()
    .removeFootprint(entity, old)
    .addFootprint(entity, new)
    .build()
