package ofws.core.game.map

import ofws.math.map.TileMap

data class GameMap(
    val tilemap: TileMap<Terrain>,
    val entityMap: EntityMap,
) {

    constructor(tilemap: TileMap<Terrain>) : this(tilemap, EntityMap(tilemap.size))

}