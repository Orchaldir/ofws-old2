package ofws.app

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.stage.Stage
import ofws.app.rendering.CanvasRenderer
import ofws.core.render.Renderer
import ofws.core.render.TileRenderer
import ofws.math.Size

abstract class TileApplication(
    sizeX: Int,
    sizeY: Int,
    tileWidth: Int,
    tileHeight: Int
) : Application() {
    val size = Size(sizeX, sizeY)
    private val tileSize = Size(tileWidth, tileHeight)
    private var canvasRenderer: CanvasRenderer? = null
    val renderer: Renderer
        get() = canvasRenderer as Renderer
    lateinit var tileRenderer: TileRenderer

    private var lastX = -1
    private var lastY = -1

    protected fun init(
        primaryStage: Stage,
        windowTitle: String
    ): Scene {

        val canvasWidth = size.x * tileSize.x.toDouble()
        val canvasHeight = size.y * tileSize.y.toDouble()

        val root = Group()
        val canvas = Canvas(canvasWidth, canvasHeight)
        root.children.add(canvas)
        val windowScene = Scene(root, Color.BLACK)

        with(primaryStage) {
            title = windowTitle
            scene = windowScene
            isResizable = false
            show()
        }

        canvasRenderer = CanvasRenderer(canvas.graphicsContext2D)
        tileRenderer = TileRenderer(renderer, 0, 0, tileSize)

        windowScene.onKeyReleased = EventHandler { event: KeyEvent ->
            onKeyReleased(event.code)
        }

        windowScene.onMouseClicked = EventHandler { event ->
            onTileClicked(
                tileRenderer.getX(event.x.toInt()),
                tileRenderer.getY(event.y.toInt()),
                event.button
            )
        }

        windowScene.onMouseMoved = EventHandler { event ->
            val x = tileRenderer.getX(event.x.toInt())
            val y = tileRenderer.getY(event.y.toInt())

            if (x != lastX || y != lastY) {
                onMouseMoved(x, y)
                lastX = x
                lastY = y
            }
        }

        return windowScene
    }

    open fun onKeyReleased(keyCode: KeyCode) {
        // Should be overwritten by subclasses if needed
    }

    open fun onTileClicked(x: Int, y: Int, button: MouseButton) {
        // Should be overwritten by subclasses if needed
    }

    open fun onMouseMoved(x: Int, y: Int) {
        // Should be overwritten by subclasses if needed
    }
}