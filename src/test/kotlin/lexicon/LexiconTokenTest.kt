package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconTokenTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "token",
  "description": "User clicked through to the feed item"
}            
        """.trimIndent()
        val parsed = LexiconToken(description = "User clicked through to the feed item")
        assertEquals(parsed, json.decodeFromString(raw))
    }
}