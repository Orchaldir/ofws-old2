package ofws.core.game.reducer

import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.updateFootprint
import ofws.core.game.map.EntityMap
import ofws.core.game.map.GameMap
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val UPDATE_POSITION_REDUCER: Reducer<UpdatePosition, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val storage = state.getStorage<Footprint>()!!
    val footprint = storage.getOrThrow(action.entity)

    val newFootprint = updateFootprint(footprint, action.index, action.direction)
    val newMap = updateMap(map, action.entity, footprint, newFootprint)
    val newFootprintStorage = storage.updateAndRemove(mapOf(action.entity to newFootprint))
    val newState = state.copy(listOf(newFootprintStorage), listOf(newMap))

    noFollowUps(newState)
}

fun updateMap(map: GameMap, entity: Entity, old: Footprint, new: Footprint) = map
    .copy(entityMap = updateMap(map.entityMap, entity, old, new))

fun updateMap(map: EntityMap, entity: Entity, old: Footprint, new: Footprint) = map.builder()
    .removeFootprint(entity, old)
    .addFootprint(entity, new)
    .build()
