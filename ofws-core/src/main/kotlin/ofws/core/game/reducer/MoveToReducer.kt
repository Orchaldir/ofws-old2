package ofws.core.game.reducer

import ofws.core.game.action.MoveTo
import ofws.core.game.component.*
import ofws.core.game.map.EntityMap
import ofws.core.game.map.EntityMapBuilder
import ofws.core.game.map.GameMap
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction
import ofws.math.map.TileIndex
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val MOVE_TO_REDUCER: Reducer<MoveTo, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val bodyStorage = state.getStorage<Footprint>()!!
    val footprint = bodyStorage.getOrThrow(action.entity)

    val newEntityMap = updateMap(map.entityMap, action.entity, footprint, action.index)
    val newMap = map.copy(entityMap = newEntityMap)
    val newFootprint = updateFootprint(footprint, action.index, Direction.NORTH)
    val newFootprintStorage = bodyStorage.updateAndRemove(mapOf(action.entity to newFootprint))
    val newState = state.copy(listOf(newFootprintStorage), listOf(newMap))

    noFollowUps(newState)
}

fun updateMap(map: EntityMap, entity: Entity, footprint: Footprint, position: TileIndex) =
    updateMap(map.builder(), entity, footprint, position).build()

fun updateMap(map: EntityMapBuilder, entity: Entity, footprint: Footprint, position: TileIndex) = when (footprint) {
    is SimpleFootprint -> map
        .removeEntity(footprint.position, entity)
        .addEntity(position, entity)

    is BigFootprint -> map
        .removeEntity(footprint.position, entity, footprint.size)
        .addEntity(position, entity, footprint.size)

    is SnakeFootprint -> with(map) {
        val last = footprint.positions.last()
        if (footprint.positions.count { i -> i == last } == 1) {
            removeEntity(last, entity)
        }
        addEntity(position, entity)
    }
}