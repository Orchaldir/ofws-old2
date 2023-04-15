package ofws.core.game.component

import ofws.core.render.Color
import ofws.core.render.Tile
import ofws.core.render.UnicodeTile

val DEFAULT_GRAPHIC = UnicodeTile('?', Color.PINK)

data class Graphic(val tiles: List<Tile>) {

    constructor(tile: Tile) : this(listOf(tile))

    fun get(index: Int) = tiles.getOrElse(index) { DEFAULT_GRAPHIC }

}