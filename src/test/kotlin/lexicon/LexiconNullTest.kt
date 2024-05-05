package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconNullTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "null"
}    
        """.trimIndent()
        val parsed = LexiconNull()
        assertEquals(parsed, json.decodeFromString(raw))
    }
}