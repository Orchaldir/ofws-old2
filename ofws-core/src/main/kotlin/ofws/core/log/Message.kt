package ofws.core.log

import ofws.ecs.EcsState
import ofws.ecs.Entity

enum class MessageType {
    INFO,
    WARN,
}

data class Message(val text: String, val type: MessageType)

fun inform(text: String) = Message(text, MessageType.INFO)

fun inform(state: EcsState, text: String, entity: Entity) = inform(text.format(getText(state, entity)))

fun warn(text: String) = Message(text, MessageType.WARN)

fun warn(state: EcsState, text: String, entity: Entity) = warn(text.format(getText(state, entity)))

fun getText(state: EcsState, entity: Entity) = "entity $entity"