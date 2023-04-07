package ofws.app.demo

import javafx.application.Application
import javafx.stage.Stage
import ofws.app.TileApplication
import ofws.core.render.Color

class RenderingDemo : TileApplication(50, 20, 22, 32) {

    override fun start(primaryStage: Stage) {
        init(primaryStage, "Rendering Demo")
        render()
    }

    private fun render() {
        with(renderer) {
            setColor(Color.RED)
            setFont(32)
            renderUnicode("@", 100, 100)
            renderUnicode("🌳", 200, 200)
            setColor(Color.BLUE)
            renderRectangle(400, 300, 100, 200)
        }

        with(tileRenderer) {
            renderFullTile(Color.GREEN, 5, 10)
            renderFullTile(Color.BLUE, 6, 10)
            renderUnicodeTile("@", Color.RED, 5, 11, 2)
            renderText("This is a test.", Color.WHITE, 0, 0, 1)
        }
    }
}

fun main() {
    Application.launch(RenderingDemo::class.java)
}