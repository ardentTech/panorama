package codegen

import kotlin.test.Test
import kotlin.test.assertEquals

class KPropValidatorsTest {

    @Test
    fun `gte format ok`() {
        assertEquals("require(8 >= 2)", KPropValidators.gte(8, 2))
    }

    @Test
    fun `lte format ok`() {
        assertEquals("require(4 <= 16)", KPropValidators.lte(4, 16))
    }

    @Test
    fun `membership int format ok`() {
        assertEquals("require(8 in listOf(2, 4, 8, 16, 32))", KPropValidators.membership(8, listOf(2, 4, 8, 16, 32)))
    }

    @Test
    fun `membership string format ok`() {
        assertEquals("""require("test" in listOf("foo", "bar", "test"))""", KPropValidators.membership("test", listOf("foo", "bar", "test")))
    }
}