package ofws.core.render

interface Renderer {

    fun clear()

    fun renderRectangle(x: Int, y: Int, width: Int, height: Int)
    fun renderUnicode(codePoint: Int, centerX: Int, centerY: Int)

    fun setColor(color: Color)
    fun setFont(size: Int, name: String = "Liberation Mono")

}