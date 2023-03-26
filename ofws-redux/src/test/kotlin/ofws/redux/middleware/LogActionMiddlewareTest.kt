package ofws.redux.middleware

import ofws.redux.DefaultStore
import ofws.redux.noFollowUps
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LogActionMiddlewareTest {

    @Test
    fun `Test logging of actions`() {
        val store = DefaultStore<Int, Int>(
            10, { state,
                  action ->
                noFollowUps(state + action)
            },
            listOf(::logAction)
        )

        store.dispatch(3)

        Assertions.assertEquals(13, store.getState())
    }

}