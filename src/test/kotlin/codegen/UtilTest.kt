package codegen

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilTest {

    @Test
    fun `camelToEnumCase ok`() {
        assertEquals("FOO_BAR", "fooBar".camelToEnumCase())
    }

    @Test
    fun `capitalize first char lowercase`() {
        assertEquals("Foobar", "foobar".capitalize())
    }

    @Test
    fun `capitalize first char uppercase`() {
        assertEquals("Foobar", "Foobar".capitalize())
    }


    @Test
    fun `uncapitalize first char lowercase`() {
        assertEquals("foobar", "foobar".uncapitalize())
    }

    @Test
    fun `uncapitalize first char uppercase`() {
        assertEquals("foobar", "Foobar".uncapitalize())
    }
}