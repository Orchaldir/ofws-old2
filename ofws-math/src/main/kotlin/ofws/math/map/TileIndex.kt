package ofws.math.map

/**
 * The index of a tile in a [TileMap].
 */
@JvmInline
value class TileIndex(val index: Int)

fun toList(vararg indices: Int) = indices.map { TileIndex(it) }

fun toSet(vararg indices: Int) = indices.map { TileIndex(it) }.toSet()