package ofws.core.log

import ofws.ecs.EcsBuilder
import ofws.ecs.EcsState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageLogTest {

    val message0 = inform("message0")
    val message1 = inform("message1")
    val message2 = inform("message2")
    val log = MessageLog(message2)

    @Test
    fun `Add a message`() {
        assertEquals(MessageLog(listOf(message2, message0)), log.add(message0))
    }

    @Test
    fun `Add messages`() {
        assertEquals(MessageLog(listOf(message2, message1, message0)), log.add(listOf(message1, message0)))
    }

    @Test
    fun `Add a message to an ecs state`() {
        val state = with(EcsBuilder()) {
            addData(log)
            build()
        }
        val newState = addMessage(state, message1)

        assertEquals(MessageLog(listOf(message2, message1)), newState.getData<MessageLog>())
    }

    @Test
    fun `Add a message to an ecs state without log`() {
        val state = EcsState()
        val newState = addMessage(state, message1)

        assertEquals(MessageLog(message1), newState.getData<MessageLog>())
    }

}