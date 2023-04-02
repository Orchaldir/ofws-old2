package ofws.ecs

import ofws.ecs.storage.ComponentMap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EcsBuilderTest {

    private val e0 = Entity(0)
    private val e1 = Entity(1)
    private val e2 = Entity(2)
    private val component0 = "Example Component"
    private val component1 = "Another Component"
    private val storage0 = ComponentMap("String", mapOf(e0 to component0))
    private val entities = mutableSetOf(e0, e1)

    @Test
    fun `Create an entity builder`() {
        val builder = EcsBuilder(storageMap = mutableMapOf("String" to storage0))

        assertEquals(EntityBuilder(e0, mutableMapOf("String" to storage0)), builder.createEntity())
        assertEquals(EntityBuilder(e1, mutableMapOf("String" to storage0)), builder.createEntity())

        assertEquals(EcsBuilder(e2, entities, mutableMapOf("String" to storage0)), builder)
    }

    @Test
    fun `Register a component`() {
        val builder = EcsBuilder(storageMap = mutableMapOf("String" to storage0))
        val storage = ComponentMap<Int>("Int")
        val result = EcsBuilder(storageMap = mutableMapOf("String" to storage0, "Int" to storage))

        builder.registerComponent<Int>()

        assertEquals(result, builder)
    }

    @Test
    fun `Build the ECS state`() {
        val builder = EcsBuilder(e2, entities, mutableMapOf("String" to storage0), mutableMapOf("Int" to 42))
        val state = EcsState(entities, mutableMapOf("String" to storage0), mutableMapOf("Int" to 42))

        assertEquals(state, builder.build())
    }

    @Test
    fun `Add data`() {
        val builder = EcsBuilder(dataMap = mutableMapOf("String" to "Test"))
        val result = EcsBuilder(dataMap = mutableMapOf("String" to "Test", "Int" to 42))

        builder.addData(42)

        assertEquals(result, builder)
    }

}