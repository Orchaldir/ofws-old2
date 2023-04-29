package ofws.core.game.action

import ofws.ecs.Entity
import ofws.math.Direction
import ofws.math.map.TileIndex

sealed class Action
object Init : Action()
data class Move(val entity: Entity, val direction: Direction) : Action()
data class UpdatePosition(val entity: Entity, val index: TileIndex, val direction: Direction) : Action()