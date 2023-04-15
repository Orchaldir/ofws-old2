package ofws.math.fov

import ofws.math.map.TileIndex

interface FovAlgorithm {

    fun calculateVisibleCells(config: FovConfig): Set<TileIndex>

}