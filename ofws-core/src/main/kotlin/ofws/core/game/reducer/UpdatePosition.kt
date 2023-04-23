package ofws.core.game.reducer

import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.getSize
import ofws.core.game.component.updateFootprint
import ofws.core.game.map.*
import ofws.core.log.addMessage
import ofws.core.log.warn
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction
import ofws.math.map.TileIndex
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val UPDATE_POSITION_REDUCER: Reducer<UpdatePosition, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val storage = state.getStorage<Footprint>()!!
    val footprint = storage.getOrThrow(action.entity)
    val walkability = checkPosition(map, action.entity, footprint, action.index)

    updatePosition(state, map, action.entity, action.direction, footprint, walkability)
}

fun <Action> updatePosition(
    state: EcsState,
    map: GameMap,
    entity: Entity,
    direction: Direction,
    footprint: Footprint,
    walkability: Walkability,
): Pair<EcsState, List<Action>> {
    val storage = state.getStorage<Footprint>()!!

    val newState = when (walkability) {
        is Walkable -> {
            val newFootprint = updateFootprint(footprint, walkability.position, direction)
            val newMap = updateMap(map, entity, footprint, newFootprint)
            val newFootprintStorage = storage.updateAndRemove(mapOf(entity to newFootprint))
            state.copy(listOf(newFootprintStorage), listOf(newMap))
        }

        else -> handleError(state, walkability)
    }

    return noFollowUps(newState)
}

private fun checkPosition(map: GameMap, entity: Entity, footprint: Footprint, position: TileIndex) =
    map.checkWalkability(
        position,
        entity,
        getSize(footprint),
    )

private fun updateMap(map: GameMap, entity: Entity, old: Footprint, new: Footprint) = map
    .copy(entityMap = updateMap(map.entityMap, entity, old, new))

private fun updateMap(map: EntityMap, entity: Entity, old: Footprint, new: Footprint) = map.builder()
    .removeFootprint(entity, old)
    .addFootprint(entity, new)
    .build()

private fun handleError(state: EcsState, walkability: Walkability) = when (walkability) {
    BlockedByObstacle -> addMessage(state, warn("Blocked by obstacle"))
    is BlockedByEntity -> addMessage(state, warn(state, "Blocked by %s", walkability.entity))
    OutsideMap -> addMessage(state, warn("Blocked by map border"))
    else -> throw IllegalArgumentException("Unknown error!")
}