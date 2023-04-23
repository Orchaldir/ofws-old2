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
    state.getStorage<Footprint>() ?: run {
        logger.info("Skip init of missing footprints")
        return
    }
    val builder = map.entityMap.builder()

    for ((entity, footprint) in state.query1<Footprint>()) {
        logger.info("Init $footprint for $entity")
        builder.addFootprint(entity, footprint)
    }

    updatedData += map.copy(entityMap = builder.build())
}
