package ofws.core.game.reducer

import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.updateFootprint
import ofws.core.game.map.EntityMap
import ofws.core.game.map.GameMap
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val UPDATE_POSITION_REDUCER: Reducer<UpdatePosition, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val bodyStorage = state.getStorage<Footprint>()!!
    val footprint = bodyStorage.getOrThrow(action.entity)

    val newFootprint = updateFootprint(footprint, action.index, Direction.NORTH)
    val newEntityMap = updateMap(map.entityMap, action.entity, footprint, newFootprint)
    val newMap = map.copy(entityMap = newEntityMap)
    val newFootprintStorage = bodyStorage.updateAndRemove(mapOf(action.entity to newFootprint))
    val newState = state.copy(listOf(newFootprintStorage), listOf(newMap))

    noFollowUps(newState)
}

fun updateMap(map: EntityMap, entity: Entity, old: Footprint, new: Footprint) = map.builder()
    .removeFootprint(entity, old)
    .addFootprint(entity, new)
    .build()
