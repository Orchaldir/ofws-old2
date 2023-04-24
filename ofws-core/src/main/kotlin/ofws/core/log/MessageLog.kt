package ofws.core.log

import ofws.ecs.EcsState

data class MessageLog(val messages: List<Message>) {

    constructor() : this(emptyList())

    constructor(message: Message) : this(listOf(message))

    fun add(message: Message) = MessageLog(messages + message)

    fun add(newMessages: List<Message>) = MessageLog(messages + newMessages)

}

fun addMessage(state: EcsState, message: Message): EcsState {
    val messageLog = state.getData() ?: MessageLog()
    return state.copy(updatedData = listOf(messageLog.add(message)))
}