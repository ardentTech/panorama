package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconIntegerTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "integer",
  "minimum": 1,
  "maximum": 100,
  "default": 50
}  
        """.trimIndent()
        val parsed = LexiconInteger(default = 50, maximum = 100, minimum = 1)
        assertEquals(parsed, json.decodeFromString(raw))
    }
}