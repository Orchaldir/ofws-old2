package ofws.ecs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeTest {

    @Test
    fun `Get simple name`() {
        assertEquals("Int", getType(Int::class))
    }

    @Test
    fun `Get with anonymous object`() {
        val anonymous = object {}

        assertTrue(getType(anonymous::class).startsWith("class ofws.ecs.TypeTest"))
    }

}