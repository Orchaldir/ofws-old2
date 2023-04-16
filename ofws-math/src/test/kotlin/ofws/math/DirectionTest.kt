package ofws.math

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class DirectionTest {

    @ParameterizedTest
    @EnumSource(Direction::class)
    fun `Each direction matches itself`(d: Direction) {
        assertTrue(d.isMatch(d.x, d.y))
    }

    @ParameterizedTest
    @EnumSource(Direction::class)
    fun `Directions don't match other values`(d: Direction) {
        assertFalse(d.isMatch(2, 1))
    }

}