package ofws.ecs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeTest {

    @Test
    fun `Get correct type`() {
        assertEquals("Int",getType(Int::class))
    }

}