package ofws.core.game.map

enum class Terrain {
    FLOOR,
    WALL;

    fun isWalkable() = this == FLOOR
}