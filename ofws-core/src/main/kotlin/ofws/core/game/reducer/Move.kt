package ofws.core.game.reducer

import ofws.core.game.action.Move
import ofws.core.game.component.*
import ofws.core.game.map.*
import ofws.core.log.addMessage
import ofws.core.log.inform
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.ecs.storage.ComponentStorage
import ofws.math.Direction
import ofws.redux.Reducer
import ofws.redux.noFollowUps

val MOVE_REDUCER: Reducer<Move, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val footprintStorage = state.getStorage<Footprint>()!!
    val footprint = footprintStorage.getOrThrow(action.entity)

    val walkability = getNewPosition(map, action.entity, footprint, action.direction)

    val newState = when (walkability) {
        is Walkable -> move(state, map, action.entity, footprintStorage, footprint, walkability, action.direction)
        else -> handleError(state, walkability)
    }

    noFollowUps(newState)
}

fun getNewPosition(map: GameMap, entity: Entity, footprint: Footprint, direction: Direction) = when (footprint) {
    is SimpleFootprint -> map.getSize().getNeighbor(footprint.position, direction) then { position ->
        map.checkWalkability(
            position,
            entity = entity
        )
    }

    is BigFootprint -> map.getSize().getNeighbor(footprint.position, direction) then { position ->
        map.checkWalkability(
            position,
            size = footprint.size,
            entity = entity
        )
    }

    is SnakeFootprint -> map.getSize().getNeighbor(footprint.positions.first(), direction) then { position ->
        map.checkWalkability(
            position,
            entity = entity
        )
    }
}

private fun move(
    state: EcsState,
    map: GameMap,
    entity: Entity,
    storage: ComponentStorage<Footprint>,
    footprint: Footprint,
    walkable: Walkable,
    direction: Direction,
): EcsState {
    val newFootprint = updateFootprint(footprint, walkable.position, direction)
    val newMap = updateMap(map, entity, footprint, newFootprint)
    val newStorage = storage.updateAndRemove(mapOf(entity to newFootprint))

    return state.copy(listOf(newStorage), listOf(newMap))
}

fun handleError(state: EcsState, walkability: Walkability) = when (walkability) {
    BlockedByObstacle -> addMessage(state, inform("Blocked by obstacle"))
    is BlockedByEntity -> addMessage(state, inform(state, "Blocked by %s", walkability.entity))
    OutsideMap -> addMessage(state, inform("Blocked by map border"))
    else -> throw IllegalArgumentException("Unknown error!")
}