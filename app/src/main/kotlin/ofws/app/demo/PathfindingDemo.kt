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
import ofws.core.log.inform
import ofws.core.render.*
import ofws.core.render.Color.Companion.BLACK
import ofws.core.render.Color.Companion.BLUE
import ofws.core.render.Color.Companion.GREEN
import ofws.core.render.Color.Companion.RED
import ofws.ecs.Entity
import ofws.math.Rectangle
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size1d.Companion.THREE
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import ofws.math.pathfinding.AStarAlgorithm
import ofws.math.pathfinding.NotSearched
import ofws.math.pathfinding.Path
import ofws.math.pathfinding.PathfindingResult
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
    private var messageLog = MessageLog(
        listOf(
            inform("Right click on the map to select the start"),
            inform("Left click on the map to select the goal"),
            inform("Press 1-3 to change the size"),
            inform("Press F1-F4 to switch rendering mode"),
        )
    )

    private lateinit var gameRenderer: GameRenderer
    private lateinit var messageRenderer: MessageLogRenderer
    private val pathfinding = AStarAlgorithm()

    private enum class RenderMode {
        RENDER_MAP,
        RENDER_OCCUPANCY_MAP,
        RENDER_COST,
        RENDER_HEURISTIC,
    }

    // options
    private var renderMode = RenderMode.RENDER_MAP
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

        val path = findPath()

        when (renderMode) {
            RenderMode.RENDER_MAP -> gameRenderer.renderMap(gameMap, ::getTile)
            RenderMode.RENDER_OCCUPANCY_MAP -> gameRenderer.renderOccupancyMap(occupancyMap)
            RenderMode.RENDER_COST -> gameRenderer.renderInts(mapSize, pathfinding.getCostsToNode(), ::getTileForCost)
            RenderMode.RENDER_HEURISTIC -> gameRenderer.renderInts(mapSize, pathfinding.getHeuristics(), ::getTileForCost)
        }

        if (path is Path) {
            path.indices.forEach { renderNode(it, "P", BLUE, ONE) }
        }

        renderNode(start, "S", GREEN, pathSize)
        renderNode(goal, "G", RED, pathSize)

        messageRenderer.render(messageLog)

        logger.info("render(): finished")
    }

    private fun findPath(): PathfindingResult {
        if (start != null && goal != null) {
            return pathfinding.find(occupancyMap, start!!, goal!!, pathSize)
        }

        return NotSearched
    }

    private fun renderNode(position: TileIndex?, tile: String, color: Color, nodeSize: Size1d) {
        if (position is TileIndex) {
            val (x, y) = gameRenderer.area.convertToParent(position)
            tileRenderer.renderFullTile(BLACK, x, y, nodeSize)
            tileRenderer.renderText(tile, color, x, y, nodeSize)
        }
    }

    override fun onKeyReleased(keyCode: KeyCode) {
        logger.info("onKeyReleased(): keyCode=$keyCode")

        when (keyCode) {
            KeyCode.DIGIT1 -> pathSize = ONE
            KeyCode.DIGIT2 -> pathSize = TWO
            KeyCode.DIGIT3 -> pathSize = THREE
            KeyCode.F1 -> renderMode = RenderMode.RENDER_MAP
            KeyCode.F2 -> renderMode = RenderMode.RENDER_OCCUPANCY_MAP
            KeyCode.F3 -> renderMode = RenderMode.RENDER_COST
            KeyCode.F4 -> renderMode = RenderMode.RENDER_HEURISTIC
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

    private fun getTileForCost(factor: Double) = FullTile(RED.lerp(GREEN, factor))
}

fun main() {
    Application.launch(PathfindingDemo::class.java)
}