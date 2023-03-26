package ofws.redux.middleware

import mu.KotlinLogging
import ofws.redux.Dispatcher

private val logger = KotlinLogging.logger {}

fun <Action, State> logAction(
    dispatcher: Dispatcher<Action>,
    @Suppress("UNUSED_PARAMETER") supplier: () -> State
): Dispatcher<Action> {
    return { action ->
        logger.info("Dispatch $action")
        dispatcher(action)
    }
}