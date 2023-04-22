package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.component.Footprint
import ofws.core.game.component.Graphic
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.render.Color
import ofws.core.render.FullTile
import ofws.core.render.GameRenderer
import ofws.core.render.UnicodeTile
import ofws.ecs.EcsBuilder
import ofws.ecs.EcsState
import ofws.math.Range
import ofws.math.fov.FovConfig
import ofws.math.fov.ShadowCasting
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import kotlin.random.Random
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

class FieldOfViewDemo : TileApplication(60, 45, 20, 20) {
    private val map = with(TileMapBuilder(size, Terrain.FLOOR)) {
        addBorder(Terrain.WALL)
        val rng = Random
        repeat(500) { setTile(TileIndex(rng.nextInt(size.tiles)), Terrain.WALL) }
        build()
    }
    private val gameMap = GameMap(map)
    private lateinit var gameRenderer: GameRenderer
    private lateinit var state: EcsState

    private var fovIndex = size.getIndex(30, 22)
    private val fovAlgorithm = ShadowCasting()
    private var visibleTiles = setOf<TileIndex>()
    private val knownTiles = mutableSetOf<TileIndex>()
    private val floorTile = UnicodeTile('.', Color.WHITE)
    private val wallTile = FullTile(Color.WHITE)
    private val visibleTile = FullTile(Color.GREEN)

    override fun start(primaryStage: Stage) {
        init(primaryStage, "FieldOfView Demo")

        gameRenderer = GameRenderer(map.size, tileRenderer)

        state = with(EcsBuilder()) {
            with(createEntity()) {
                add(SimpleFootprint(fovIndex) as Footprint)
                add(Graphic(FullTile(Color.BLUE)))
            }
            build()
        }

        update()
    }

    private fun update() {
        logger.info("update()")

        val config = FovConfig(map.size, fovIndex, Range(max = 10), ::isBlocking)

        visibleTiles = fovAlgorithm.calculateVisibleCells(config)
        knownTiles.addAll(visibleTiles)

        render()
    }

    private fun render() {
        logger.info("render()")

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

        val newIndex = map.size.getIndex(x, y)

        if (!isBlocking(newIndex)) {
            fovIndex = newIndex
            update()
        }
    }

    private fun isBlocking(index: TileIndex) =
        map.getTile(index) == Terrain.WALL

    private fun getTile(terrain: Terrain) = if (terrain == Terrain.FLOOR) {
        floorTile
    } else {
        wallTile
    }
}

fun main() {
    Application.launch(FieldOfViewDemo::class.java)
}