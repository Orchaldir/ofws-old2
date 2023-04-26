package ofws.core.render

import ofws.core.log.MessageLog
import ofws.core.log.MessageType
import ofws.core.log.MessageType.WARN
import ofws.math.Rectangle
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import kotlin.math.log10

data class MessageLogRenderer(
    private val area: Rectangle,
    private val tileRenderer: TileRenderer,
    private val colors: Map<MessageType, Color> = mapOf(WARN to Color.YELLOW),
    private val fontSize: Size1d = ONE
) {

    fun render(messageLog: MessageLog) {
        val size = messageLog.messages.size
        val digits = size.countDigits()

        for ((i, message) in messageLog.messages.reversed().withIndex()) {
            if (i >= area.size.y) {
                return
            }

            val y = area.startY + i * fontSize.size
            val color = colors.getOrDefault(message.type, Color.WHITE)
            val text = String.format("%${digits}d: %s", size - i, message.text).take(area.size.x)
            tileRenderer.renderText(text, color, area.startX, y, fontSize)
        }
    }

}

private fun Int.countDigits() = when (this) {
    0 -> 1
    else -> log10(toDouble()).toInt() + 1
}