package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconRefTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "ref",
  "ref": "#listPurpose"
}    
        """.trimIndent()
        val parsed = LexiconRef(ref = "#listPurpose")
        assertEquals(parsed, json.decodeFromString(raw))
    }
}