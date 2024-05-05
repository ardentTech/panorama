package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconCidLinkTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "cid-link"
}    
        """.trimIndent()
        val parsed = LexiconCidLink()
        assertEquals(parsed, json.decodeFromString(raw))
    }
}