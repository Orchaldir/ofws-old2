package ofws.core.game.map

import ofws.ecs.Entity
import ofws.math.map.TileIndex

/**
 * The result of a check, if an entity can move to a specific tile.
 */
sealed class Walkability
data class Walkable(val position: TileIndex) : Walkability()
object BlockedByObstacle : Walkability()
data class BlockedByEntity(val entity: Entity) : Walkability()
object OutsideMap : Walkability()

infix fun TileIndex?.then(f: (TileIndex) -> Walkability) =
    when (this) {
        null -> OutsideMap
        else -> f(this)
    }