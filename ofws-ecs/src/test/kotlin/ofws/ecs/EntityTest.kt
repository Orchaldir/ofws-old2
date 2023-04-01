package ofws.ecs

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class EntityTest {

    @Test
    fun `Next entity has an id higher by 1`() {
        assertEquals(Entity(5), Entity(4).next())
    }

}