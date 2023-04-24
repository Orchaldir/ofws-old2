package ofws.core.log

enum class MessageType {
    INFO,
    WARN,
}

data class Message(val text: String, val type: MessageType)

fun inform(text: String) = Message(text, MessageType.INFO)

fun warn(text: String) = Message(text, MessageType.WARN)
