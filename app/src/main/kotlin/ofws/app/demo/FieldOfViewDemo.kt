package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.action.Action
import ofws.core.game.action.Init
import ofws.core.game.action.Move
import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.Footprint
import ofws.core.game.component.Graphic
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.getPosition
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.game.reducer.INIT_REDUCER
import ofws.core.game.reducer.MOVE_REDUCER
import ofws.core.game.reducer.UPDATE_POSITION_REDUCER
import ofws.core.log.MessageLog
import ofws.core.log.inform
import ofws.core.render.*
import ofws.ecs.EcsBuilder
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction.*
import ofws.math.Range
import ofws.math.Rectangle
import ofws.math.Size2d
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

private const val LOG_SIZE = 5

class FieldOfViewDemo : TileApplication(60, 45, 20, 20) {
    private val mapSize = Size2d(size.x, size.y - LOG_SIZE)
    private lateinit var gameRenderer: GameRenderer
    private lateinit var messageRenderer: MessageLogRenderer
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

        val map = with(TileMapBuilder(mapSize, Terrain.FLOOR)) {
            addBorder(Terrain.WALL)
            val rng = Random
            repeat(500) { setTile(TileIndex(rng.nextInt(mapSize.tiles)), Terrain.WALL) }
            build()
        }

        gameRenderer = GameRenderer(Rectangle(0, LOG_SIZE, mapSize), tileRenderer)
        messageRenderer = MessageLogRenderer(Rectangle(Size2d(size.x, LOG_SIZE)), tileRenderer)

        val ecsState = with(EcsBuilder()) {
            addData(GameMap(map))
            addData(MessageLog(listOf(inform("Welcome! Use the arrow keys to move around."))))
            val entity = createEntity()
                .add(SimpleFootprint(mapSize.getIndex(30, 22)) as Footprint)
                .add(Graphic(FullTile(Color.BLUE)))
                .entity
            addData(entity)
            build()
        }

        val reducer: Reducer<Action, EcsState> = { state, action ->
            when (action) {
                is Init -> INIT_REDUCER(state, action)
                is Move -> MOVE_REDUCER(state, action)
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
        val config = FovConfig(mapSize, index, Range(max = 10), createIsBlocking(state))

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

        messageRenderer.render(state.getData()!!)

        logger.info("render(): finished")
    }

    override fun onKeyReleased(keyCode: KeyCode) {
        logger.info("onKeyReleased(): keyCode=$keyCode")
        val entity = store.getState().getData<Entity>()!!

        when (keyCode) {
            KeyCode.UP -> store.dispatch(Move(entity, NORTH))
            KeyCode.RIGHT -> store.dispatch(Move(entity, EAST))
            KeyCode.DOWN -> store.dispatch(Move(entity, SOUTH))
            KeyCode.LEFT -> store.dispatch(Move(entity, WEST))
            KeyCode.ESCAPE -> exitProcess(0)
            else -> return
        }
    }

    override fun onTileClicked(x: Int, y: Int, button: MouseButton) {
        logger.info("onTileClicked(): x=$x y=$y button=$button")

        val newIndex = gameRenderer.area.convertToInside(x, y) ?: return
        val isBlocking = createIsBlocking(store.getState())

        if (!isBlocking(newIndex)) {
            val state = store.getState()
            val entity = state.getData<Entity>()!!
            store.dispatch(UpdatePosition(entity, newIndex, NORTH))
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