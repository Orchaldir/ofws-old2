package ofws.core.game.reducer

import mu.KotlinLogging
import ofws.core.game.action.Init
import ofws.core.game.component.Footprint
import ofws.core.game.map.GameMap
import ofws.ecs.EcsState
import ofws.redux.Reducer
import ofws.redux.noFollowUps

private val logger = KotlinLogging.logger {}


val INIT_REDUCER: Reducer<Init, EcsState> = { state, _ ->
    val updatedData = mutableListOf<Any>()

    initFootprints(state, updatedData)

    noFollowUps(state.copy(updatedData = updatedData))
}

private fun initFootprints(state: EcsState, updatedData: MutableList<Any>) {
    val map = state.getData<GameMap>() ?: run {
        logger.info("Skip init of footprints, because of missing map")
        return
    }
    val footprintStorage = state.getStorage<Footprint>() ?: return
    val mapBuilder = map.entityMap.builder()

    for (entity in footprintStorage.getIds()) {
        val footprint = footprintStorage.getOrThrow(entity)
        logger.info("Init $footprint for $entity")
        mapBuilder.addFootprint(entity, footprint)
    }

    updatedData += map.copy(entityMap = mapBuilder.build())
}
