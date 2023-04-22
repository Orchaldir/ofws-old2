package ofws.core.game.reducer

import mu.KotlinLogging
import ofws.core.game.action.Init
import ofws.core.game.component.BigFootprint
import ofws.core.game.component.Footprint
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.SnakeFootprint
import ofws.core.game.map.EntityMapBuilder
import ofws.core.game.map.GameMap
import ofws.ecs.EcsState
import ofws.ecs.Entity
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
        addToMap(mapBuilder, entity, footprint)
    }

    updatedData += map.copy(entityMap = mapBuilder.build())
}

private fun addToMap(builder: EntityMapBuilder, entity: Entity, body: Footprint) = when (body) {
    is SimpleFootprint -> builder
        .addEntity(index = body.position, entity = entity)

    is BigFootprint -> builder
        .addEntity(index = body.position, entity = entity, size = body.size)

    is SnakeFootprint -> {
        body.positions.forEach { p -> builder.addEntity(p, entity) }
        builder
    }
}