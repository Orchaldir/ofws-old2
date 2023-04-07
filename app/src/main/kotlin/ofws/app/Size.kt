package ofws.app

import ofws.core.requireGreater

data class Size(
    val x: Int,
    val y: Int
) {

    init {
        requireGreater(x, 0, "x")
        requireGreater(y, 0, "y")
    }

    val cells: Int
        get() = x * y

}