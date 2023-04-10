package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.map.Terrain
import ofws.core.render.Color
import ofws.math.Position
import ofws.math.map.TileMapBuilder
import kotlin.random.Random
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

class FieldOfViewDemo : TileApplication(60, 45, 20, 20) {
    private val map = with(TileMapBuilder(size, Terrain.FLOOR)) {
        addBorder(Terrain.WALL)
        val rng = Random
        repeat(500) { setTile(Position(rng.nextInt(size.tiles)), Terrain.WALL) }
        build()
    }

    private var position = Position(990)

    override fun start(primaryStage: Stage) {
        init(primaryStage, "FieldOfView Demo")
        update()
    }

    private fun update() {
        logger.info("update()")

        render()
    }

    private fun render() {
        logger.info("render()")

        renderer.clear()

        renderNode(position, Color.BLUE)

        logger.info("render(): finished")
    }

    private fun renderNode(position: Position, color: Color) {
        tileRenderer.renderFullTile(color, size.getX(position), size.getY(position))
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
    }
}

fun main() {
    Application.launch(FieldOfViewDemo::class.java)
}