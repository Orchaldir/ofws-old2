package ofws.core.game.map

import ofws.math.map.TileIndex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class WalkabilityTest {

    @Nested
    inner class Then {

        private val check: (TileIndex) -> Walkability = { i -> Walkable(i) }

        @Test
        fun `Null become Outside Map`() {
            val index: TileIndex? = null

            assertEquals(OutsideMap, index then check)
        }

        @Test
        fun `Values are mapped`() {
            val index = TileIndex(42)

            assertEquals(Walkable(index), index then check)
        }

    }

}