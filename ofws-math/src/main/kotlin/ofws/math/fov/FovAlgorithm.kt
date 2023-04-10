package ofws.math.fov

import ofws.math.Position

interface FovAlgorithm {

    fun calculateVisibleCells(config: FovConfig): Set<Position>

}