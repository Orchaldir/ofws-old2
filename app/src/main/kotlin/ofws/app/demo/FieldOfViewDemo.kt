package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.action.Action
import ofws.core.game.action.Init
import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.Graphic
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.getPosition
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.game.reducer.INIT_REDUCER
import ofws.core.game.reducer.UPDATE_POSITION_REDUCER
import ofws.core.render.Color
import ofws.core.render.FullTile
import ofws.core.render.GameRenderer
import ofws.core.render.UnicodeTile
import ofws.ecs.EcsBuilder
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Range
import ofws.math.fov.FovConfig
import ofws.math.fov.ShadowCasting
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import ofws.redux.DefaultStore
import ofws.redux.Reducer
import ofws.redux.middleware.logAction
import ofws.redux.noFollowUps
import kotlin.random.Random
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

class FieldOfViewDemo : TileApplication(60, 45, 20, 20) {
    private lateinit var gameRenderer: GameRenderer
    private lateinit var store: DefaultStore<Action, EcsState>

    private val fovAlgorithm = ShadowCasting()
    private var visibleTiles = setOf<TileIndex>()
    private val knownTiles = mutableSetOf<TileIndex>()

    // render config
    private val floorTile = UnicodeTile('.', Color.WHITE)
    private val wallTile = FullTile(Color.WHITE)
    private val visibleTile = FullTile(Color.GREEN)

    override fun start(primaryStage: Stage) {
        init(primaryStage, "FieldOfView Demo")

        val map = with(TileMapBuilder(size, Terrain.FLOOR)) {
            addBorder(Terrain.WALL)
            val rng = Random
            repeat(500) { setTile(TileIndex(rng.nextInt(size.tiles)), Terrain.WALL) }
            build()
        }

        gameRenderer = GameRenderer(size, tileRenderer)

        val ecsState = with(EcsBuilder()) {
            addData(GameMap(map))
            val entity = createEntity()
                .add(SimpleFootprint(size.getIndex(30, 22)) as Footprint)
                .add(Graphic(FullTile(Color.BLUE)))
                .entity
            addData(entity)
            build()
        }

        val reducer: Reducer<Action, EcsState> = { state, action ->
            when (action) {
                is Init -> INIT_REDUCER(state, action)
                is UpdatePosition -> UPDATE_POSITION_REDUCER(state, action)
                else -> noFollowUps(state)
            }
        }

        store = DefaultStore(ecsState, reducer, listOf(::logAction))
        store.subscribe(this::update)
        store.dispatch(Init)
    }

    private fun update(state: EcsState) {
        logger.info("update()")
        val entity = state.getData<Entity>()!!
        val footprint = state.getStorage<Footprint>()?.get(entity)!!
        val index = getPosition(footprint)
        val config = FovConfig(size, index, Range(max = 10), createIsBlocking(state))

        visibleTiles = fovAlgorithm.calculateVisibleCells(config)
        knownTiles.addAll(visibleTiles)

        render(state)
    }

    private fun render(state: EcsState) {
        logger.info("render()")
        val gameMap = state.getData<GameMap>()!!

        renderer.clear()

        with(gameRenderer) {
            renderTiles(gameMap, { visibleTile }, visibleTiles)
            renderEntities(state)
            renderTiles(gameMap, ::getTile, knownTiles)
        }

        logger.info("render(): finished")
    }

    override fun onKeyReleased(keyCode: KeyCode) {
        logger.info("onKeyReleased(): keyCode=$keyCode")

        when (keyCode) {
            KeyCode.ESCAPE -> exitProcess(0)
            else -> return
        }
    }

    override fun onTileClicked(x: Int, y: Int, button: MouseButton) {
        logger.info("onTileClicked(): x=$x y=$y button=$button")

        val newIndex = size.getIndex(x, y)
        val isBlocking = createIsBlocking(store.getState())

        if (!isBlocking(newIndex)) {
            val state = store.getState()
            val entity = state.getData<Entity>()!!
            store.dispatch(UpdatePosition(entity, newIndex))
        }
    }

    private fun createIsBlocking(state: EcsState): (index: TileIndex) -> Boolean {
        val map = state.getData<GameMap>()!!.tilemap
        return { index: TileIndex -> map.getTile(index) == Terrain.WALL }
    }

    private fun getTile(terrain: Terrain) = if (terrain == Terrain.FLOOR) {
        floorTile
    } else {
        wallTile
    }
}

fun main() {
    Application.launch(FieldOfViewDemo::class.java)
}