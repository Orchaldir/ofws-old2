package ofws.core.game.component

import ofws.math.Direction
import ofws.math.Direction.NORTH
import ofws.math.Size1d
import ofws.math.Size1d.Companion.ONE
import ofws.math.Size2d
import ofws.math.map.TileIndex

sealed class Footprint
data class SimpleFootprint(val position: TileIndex, val direction: Direction = NORTH) : Footprint()
data class BigFootprint(val position: TileIndex, val size: Size1d, val direction: Direction = NORTH) : Footprint()
data class SnakeFootprint(val positions: List<TileIndex>, val direction: Direction = NORTH) : Footprint()


fun getIndicesUnderFootprint(mapSize: Size2d, footprint: Footprint): List<TileIndex> = when (footprint) {
    is SimpleFootprint -> listOf(footprint.position)
    is BigFootprint -> mapSize.getIndices(footprint.position, footprint.size)
    is SnakeFootprint -> footprint.positions
}

fun getPosition(footprint: Footprint) = when (footprint) {
    is SimpleFootprint -> footprint.position
    is BigFootprint -> footprint.position
    is SnakeFootprint -> footprint.positions.first()
}

fun getSize(footprint: Footprint) = when (footprint) {
    is BigFootprint -> footprint.size
    else -> ONE
}

fun updateFootprint(footprint: Footprint, position: TileIndex, direction: Direction) = when (footprint) {
    is SimpleFootprint -> footprint.copy(position, direction)
    is BigFootprint -> footprint.copy(position, direction = direction)
    is SnakeFootprint -> {
        val positions = footprint.positions.toMutableList()
        positions.removeAt(positions.lastIndex)
        footprint.copy(listOf(position) + positions, direction)
    }
}
