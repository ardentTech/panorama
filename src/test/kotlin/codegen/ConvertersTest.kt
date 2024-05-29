package codegen

import lexicon.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ConvertersTest {

    @Test
    fun `toFile ok`() {
        val res = LexiconDoc.fake(
            defs = mapOf("one" to LexiconObject.fake(
                properties = mapOf("one" to LexiconString())
            )),
            id = "foo.bar.baz.testIt"
        ).toFile()

        assertEquals("foo.bar.baz", res.packageName)
        assertEquals("TestIt", res.name)
    }
}