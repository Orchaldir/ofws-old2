package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QueryTest {

    private val e0 = Entity(0)
    private val e1 = Entity(1)
    private val e2 = Entity(2)
    private val e3 = Entity(3)

    @Test
    fun `Query 2 components`() {
        val entities = mutableListOf(0, 3)
        val storageA = ComponentMap("A", mapOf(e0 to "0", e1 to "1", e3 to "3"))
        val storageB = ComponentMap("B", mapOf(e0 to "Zero", e2 to "Two", e3 to "Three"))
        val query = Query2(entities, storageA, storageB)

        assertTrue(query.hasNext())
        assertEquals(QueryEntry2(e0, "0", "Zero"), query.next())

        assertTrue(query.hasNext())
        assertEquals(QueryEntry2(e3, "3", "Three"), query.next())

        assertFalse(query.hasNext())
    }

}