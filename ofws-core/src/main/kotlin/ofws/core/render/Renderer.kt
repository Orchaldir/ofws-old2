package ofws.core.render

interface Renderer {

    fun clear()

    fun renderRectangle(x: Int, y: Int, width: Int, height: Int)
    fun renderUnicode(text: String, centerX: Int, centerY: Int)

    fun setColor(color: Color)
    fun setFont(size: Int, name: String = "Liberation Mono")

}