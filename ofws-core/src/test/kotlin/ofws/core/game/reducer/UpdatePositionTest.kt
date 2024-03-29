package ofws.core.game.reducer

import ofws.core.game.action.UpdatePosition
import ofws.core.game.component.BigFootprint
import ofws.core.game.component.Footprint
import ofws.core.game.component.SimpleFootprint
import ofws.core.game.component.SnakeFootprint
import ofws.core.game.map.EntityMap
import ofws.core.game.map.GameMap
import ofws.core.game.map.Terrain.FLOOR
import ofws.core.game.map.Terrain.WALL
import ofws.core.log.Message
import ofws.core.log.MessageLog
import ofws.core.log.warn
import ofws.ecs.EcsBuilder
import ofws.ecs.EcsState
import ofws.ecs.Entity
import ofws.math.Direction.NORTH
import ofws.math.Size1d.Companion.TWO
import ofws.math.Size2d
import ofws.math.map.TileIndex
import ofws.math.map.TileMapBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UpdatePositionTest {

    private val size = Size2d(2, 4)
    private val tilemap = TileMapBuilder(size, FLOOR)
        .setTile(0, 3, WALL)
        .build()
    private val index0 = TileIndex(0)
    private val index1 = TileIndex(1)
    private val index2 = TileIndex(2)
    private val index3 = TileIndex(3)
    private val index4 = TileIndex(4)
    private val index5 = TileIndex(5)
    private val obstacle = TileIndex(6)
    private val outside = TileIndex(8)
    private val entity = Entity(0)
    private val other = Entity(1)

    @Test
    fun `Move simple footprint`() {
        test(mapOf(index1 to entity), SimpleFootprint(index1), mapOf(index2 to entity), SimpleFootprint(index2))
    }

    @Test
    fun `Move big footprint`() {
        test(
            mapOf(
                index0 to entity,
                index1 to entity,
                index2 to entity,
                index3 to entity,
            ),
            BigFootprint(index0, TWO),
            mapOf(
                index2 to entity,
                index3 to entity,
                index4 to entity,
                index5 to entity,
            ),
            BigFootprint(index2, TWO)
        )
    }

    @Test
    fun `Move snake footprint`() {
        test(
            mapOf(
                index0 to entity,
                index1 to entity,
                index3 to entity,
            ), SnakeFootprint(listOf(index0, index1, index3)),
            mapOf(
                index2 to entity,
                index0 to entity,
                index1 to entity,
            ),
            SnakeFootprint(listOf(index2, index0, index1))
        )
    }

    @Test
    fun `Fail to move outside`() {
        testFailure(mapOf(index1 to entity), SimpleFootprint(index1), outside, warn("New position is outside the map"))
    }

    @Test
    fun `Blocked by other entity`() {
        testFailure(
            mapOf(index1 to entity, index4 to other),
            SimpleFootprint(index1),
            index4,
            warn("New position is full")
        )
    }


    @Test
    fun `Blocked by obstacle`() {
        testFailure(mapOf(index1 to entity), SimpleFootprint(index1), obstacle, warn("Blocked by obstacle"))
    }

    private fun test(
        entities: Map<TileIndex, Entity>,
        footprint: Footprint,
        newEntities: Map<TileIndex, Entity>,
        newFootprint: Footprint,
        index: TileIndex = index2,
    ): EcsState {
        val entityMap = EntityMap(size, entities)
        val map = GameMap(tilemap, entityMap)
        val state = with(EcsBuilder()) {
            addData(map)
            createEntity().add(footprint)
            build()
        }
        val (newState, actions) = UPDATE_POSITION_REDUCER(state, UpdatePosition(entity, index, NORTH))
        val newMap = newState.getData<GameMap>()!!

        assertTrue(actions.isEmpty())
        assertEquals(newFootprint, newState.getStorage<Footprint>()?.get(entity))
        assertEquals(tilemap, newMap.tilemap)
        assertEquals(EntityMap(size, newEntities), newMap.entityMap)

        return newState
    }

    private fun testFailure(
        entities: Map<TileIndex, Entity>,
        footprint: Footprint,
        index: TileIndex,
        message: Message
    ) {
        val state = test(entities, footprint, entities, footprint, index)

        assertEquals(MessageLog(listOf(message)), state.getData<MessageLog>()!!)
    }

}