package ofws.math.fov

import ofws.math.map.Index

interface FovAlgorithm {

    fun calculateVisibleCells(config: FovConfig): Set<Index>

}