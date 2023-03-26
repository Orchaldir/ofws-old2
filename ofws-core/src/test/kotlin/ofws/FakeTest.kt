package ofws

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FakeTest {

    @Test
    fun `Test fake function`() {
        Assertions.assertEquals(42, getFake())
    }

}