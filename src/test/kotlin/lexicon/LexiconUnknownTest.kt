package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconUnknownTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "unknown",
  "description": "The complete DID document for this account."
}   
        """.trimIndent()
        val parsed = LexiconUnknown(description = "The complete DID document for this account.")
        assertEquals(parsed, json.decodeFromString(raw))
    }
}