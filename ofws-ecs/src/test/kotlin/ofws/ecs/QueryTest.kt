package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryTest {

    private val e0 = Entity(0)
    private val e1 = Entity(1)
    private val e2 = Entity(2)
    private val e3 = Entity(3)
    private val sharedEntities = mutableListOf(e0, e3)

    private val storageA = ComponentMap("A", mapOf(e0 to 100, e1 to 101, e3 to 103))
    private val storageB = ComponentMap("B", mapOf(e0 to "Zero", e2 to "Two", e3 to "Three"))

    private val state = EcsState(setOf(e0, e1, e2, e3), mapOf("Int" to storageA, "String" to storageB))

    @Test
    fun `Get query of 1 component type`() {
        assertEquals(Query1(mutableListOf(e0, e1, e3), storageA), state.query1<Int>())
        assertEquals(Query1(mutableListOf(e0, e2, e3), storageB), state.query1<String>())
    }

    @Test
    fun `Get query of 2 component types`() {
        assertEquals(Query2(sharedEntities, storageA, storageB), state.query2<Int, String>())
    }

    @Test
    fun `Test query of 1 component type`() {
        val query = Query1(sharedEntities, storageB)

        assertTrue(query.hasNext())
        assertEquals(QueryEntry1(e0, "Zero"), query.next())

        assertTrue(query.hasNext())
        assertEquals(QueryEntry1(e3, "Three"), query.next())

        assertFalse(query.hasNext())
    }

    @Test
    fun `Test query of 2 component types`() {
        val query = Query2(sharedEntities, storageA, storageB)

        assertTrue(query.hasNext())
        assertEquals(QueryEntry2(e0, 100, "Zero"), query.next())

        assertTrue(query.hasNext())
        assertEquals(QueryEntry2(e3, 103, "Three"), query.next())

        assertFalse(query.hasNext())
    }

}