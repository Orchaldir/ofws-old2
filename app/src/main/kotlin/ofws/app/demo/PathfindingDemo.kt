package ofws.app.demo

import javafx.application.Application
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import mu.KotlinLogging
import ofws.app.TileApplication
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain
import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.core.log.MessageLog
import ofws.core.render.*
import ofws.ecs.Entity
import ofws.math.Rectangle
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size1d.Companion.THREE
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import ofws.math.pathfinding.AStar
import kotlin.random.Random
import kotlin.system.exitProcess


private val logger = KotlinLogging.logger {}

private const val LOG_SIZE = 5

class PathfindingDemo : TileApplication(60, 45, 20, 20) {
    private val entity = Entity(0)
    private val mapSize = Size2d(size.x, size.y - LOG_SIZE)
    private val map = with(TileMapBuilder(mapSize, FLOOR)) {
        addBorder(WALL)
        val rng = Random
        repeat(500) { setTile(TileIndex(rng.nextInt(mapSize.tiles)), WALL) }
        build()
    }
    private val gameMap = GameMap(map)
    private var occupancyMap = gameMap.createOccupancyMap(entity)
    private var messageLog = MessageLog()

    private lateinit var gameRenderer: GameRenderer
    private lateinit var messageRenderer: MessageLogRenderer
    private val pathfinding = AStar()

    // options
    private var start: TileIndex? = null
    private var goal: TileIndex? = null
    private var pathSize = ONE

    // render config
    private val floorTile = UnicodeTile('.', Color.WHITE)
    private val wallTile = FullTile(Color.WHITE)

    override fun start(primaryStage: Stage) {
        init(primaryStage, "Pathfinding Demo")

        gameRenderer = GameRenderer(Rectangle(0, LOG_SIZE, mapSize), tileRenderer)
        messageRenderer = MessageLogRenderer(Rectangle(Size2d(size.x, LOG_SIZE)), tileRenderer)

        render()
    }

    private fun render() {
        logger.info("render()")

        renderer.clear()

        gameRenderer.renderMap(gameMap, ::getTile)

        messageRenderer.render(messageLog)

        logger.info("render(): finished")
    }

    override fun onKeyReleased(keyCode: KeyCode) {
        logger.info("onKeyReleased(): keyCode=$keyCode")

        when (keyCode) {
            KeyCode.DIGIT1 -> pathSize = ONE
            KeyCode.DIGIT2 -> pathSize = TWO
            KeyCode.DIGIT3 -> pathSize = THREE
            KeyCode.ESCAPE -> exitProcess(0)
            else -> return
        }

        occupancyMap = gameMap.createOccupancyMap(entity, pathSize)

        render()
    }

    override fun onTileClicked(x: Int, y: Int, button: MouseButton) {
        logger.info("onTileClicked(): x=$x y=$y button=$button")

        if (gameRenderer.area.isInside(x, y)) {
            val position = gameRenderer.area.convertToInside(x, y)

            when (button) {
                MouseButton.PRIMARY -> start = position
                MouseButton.SECONDARY -> goal = position
                else -> return
            }

            render()
        }
    }

    private fun getTile(terrain: Terrain) = if (terrain == FLOOR) {
        floorTile
    } else {
        wallTile
    }
}

fun main() {
    Application.launch(PathfindingDemo::class.java)
}