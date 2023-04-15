package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.map.Terrain
import ofws.core.render.Color
import ofws.math.fov.FovConfig
import ofws.math.fov.ShadowCasting
import ofws.math.map.Index
import ofws.math.map.TileMapBuilder
import kotlin.random.Random
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

class FieldOfViewDemo : TileApplication(60, 45, 20, 20) {
    private val map = with(TileMapBuilder(size, Terrain.FLOOR)) {
        addBorder(Terrain.WALL)
        val rng = Random
        repeat(500) { setTile(Index(rng.nextInt(size.tiles)), Terrain.WALL) }
        build()
    }

    private var fovIndex = size.getIndex(30, 22)
    private val fovAlgorithm = ShadowCasting()
    private var visibleTiles = setOf<Index>()
    private val knownTiles = mutableSetOf<Index>()

    override fun start(primaryStage: Stage) {
        init(primaryStage, "FieldOfView Demo")
        update()
    }

    private fun update() {
        logger.info("update()")

        val config = FovConfig(map.size, fovIndex, 10, ::isBlocking)

        visibleTiles = fovAlgorithm.calculateVisibleCells(config)
        knownTiles.addAll(visibleTiles)

        render()
    }

    private fun render() {
        logger.info("render()")

        renderer.clear()

        visibleTiles.forEach { renderNode(it, Color.GREEN) }
        renderNode(fovIndex, Color.BLUE)

        for (tile in knownTiles) {
            renderTile(tile)
        }

        logger.info("render(): finished")
    }

    private fun renderNode(index: Index, color: Color) {
        tileRenderer.renderFullTile(color, size.getX(index), size.getY(index))
    }

    private fun renderTile(index: Index) {
        val symbol = if (map.getTile(index) == Terrain.FLOOR) {
            '.'
        } else {
            '#'
        }

        tileRenderer.renderUnicodeTile(symbol, Color.WHITE, map.size.getX(index), map.size.getY(index))
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

    private fun isBlocking(index: Index) =
        map.getTile(index) == Terrain.WALL
}

fun main() {
    Application.launch(FieldOfViewDemo::class.java)
}