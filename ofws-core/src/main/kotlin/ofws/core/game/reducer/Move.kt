package ofws.core.game.reducer

import ofws.core.game.action.Move
import ofws.core.game.component.Footprint
import ofws.core.game.component.getPosition
import ofws.core.game.component.getSize
import ofws.core.game.map.GameMap
import ofws.core.game.map.then
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction
import ofws.redux.Reducer

val MOVE_REDUCER: Reducer<Move, EcsState> = a@{ state, action ->
    val map = state.getData<GameMap>()!!
    val footprintStorage = state.getStorage<Footprint>()!!
    val footprint = footprintStorage.getOrThrow(action.entity)

    val walkability = getNewPosition(map, action.entity, footprint, action.direction)

    updatePosition(state, map, action.entity, action.direction, footprint, walkability)
}

fun getNewPosition(map: GameMap, entity: Entity, footprint: Footprint, direction: Direction) =
    map.getSize().getNeighbor(getPosition(footprint), direction) then { position ->
        map.checkWalkability(
            position,
            entity,
            getSize(footprint),
        )
    }