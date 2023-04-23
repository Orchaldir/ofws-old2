package ofws.core.game.reducer

import ofws.core.game.action.Init
import ofws.core.game.component.BigFootprint
import ofws.core.game.component.Footprint
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.SnakeFootprint
import ofws.core.game.map.EntityMap
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain.FLOOR
import ofws.ecs.EcsBuilder
import ofws.ecs.Entity
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InitTest {

    @Nested
    inner class InitFootprints {

        private val size = Size2d(2, 3)
        private val tilemap = TileMapBuilder(size, FLOOR).build()
        private val map = GameMap(tilemap)
        private val index0 = TileIndex(2)
        private val index1 = TileIndex(3)
        private val index2 = TileIndex(4)
        private val index3 = TileIndex(5)
        private val entity = Entity(0)

        @Test
        fun `Init simple footprint`() {
            test(SimpleFootprint(index1), mapOf(index1 to entity))
        }

        @Test
        fun `Init big footprint`() {
            test(
                BigFootprint(index0, TWO), mapOf(
                    index0 to entity,
                    index1 to entity,
                    index2 to entity,
                    index3 to entity,
                )
            )
        }

        @Test
        fun `Init snake footprint that partly overlaps`() {
            test(
                SnakeFootprint(listOf(index0, index1, index3, index3)), mapOf(
                    index0 to entity,
                    index1 to entity,
                    index3 to entity,
                )
            )
        }

        private fun test(footprint: Footprint, entities: Map<TileIndex, Entity>) {
            val state = with(EcsBuilder()) {
                addData(map)
                createEntity().add(footprint)
                build()
            }

            val (newState, actions) = INIT_REDUCER(state, Init)
            val newMap = newState.getData<GameMap>()!!

            assertTrue(actions.isEmpty())
            assertEquals(tilemap, newMap.tilemap)
            assertEquals(EntityMap(size, entities), newMap.entityMap)
        }
    }

}