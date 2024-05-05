package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconBooleanTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "boolean",
  "const": true
}    
        """.trimIndent()
        val parsed = LexiconBoolean(const = true)
        assertEquals(parsed, json.decodeFromString(raw))
    }
}