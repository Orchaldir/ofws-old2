package ofws.core.render

import ofws.core.log.MessageLog
import ofws.core.log.MessageType
import ofws.core.log.MessageType.WARN
import ofws.math.Rectangle
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE

class MessageLogRenderer(
    private val area: Rectangle,
    private val tileRenderer: TileRenderer,
    private val colors: Map<MessageType, Color> = mapOf(WARN to Color.YELLOW),
    private val fontSize: Size1d = ONE
) {

    fun render(messageLog: MessageLog) {
        for ((i, message) in messageLog.messages.reversed().withIndex()) {
            if (i >= area.size.y) {
                return
            }

            val y = area.startY + i * fontSize.size
            val color = colors.getOrDefault(message.type, Color.WHITE)
            tileRenderer.renderText(message.text, color, area.startX, y, fontSize)
        }
    }

}