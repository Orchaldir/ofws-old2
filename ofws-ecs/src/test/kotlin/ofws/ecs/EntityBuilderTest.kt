package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityBuilderTest {

    private val entity0 = Entity(42)
    private val entity1 = Entity(99)
    private val component0 = "Example Component"
    private val component1 = "Another Component"
    private val storage0 = ComponentMap("String", mapOf(entity0 to component0))

    @Test
    fun `The storage is created, if needed`() {
        val builder = EntityBuilder(entity0)

        builder.add(component0)

        val result = EntityBuilder(entity0, mutableMapOf("String" to storage0))

        assertEquals(result, builder)
    }

    @Test
    fun `An existing storage is reused`() {
        val builder = EntityBuilder(entity1, mutableMapOf("String" to storage0))

        builder.add(component1)

        val resultStorage = ComponentMap("String", mapOf(entity0 to component0, entity1 to component1))
        val result = EntityBuilder(entity1, mutableMapOf("String" to resultStorage))

        assertEquals(result, builder)
    }

}