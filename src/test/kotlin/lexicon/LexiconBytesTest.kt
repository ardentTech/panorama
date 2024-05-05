package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconBytesTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "bytes",
  "description": "CAR file containing relevant blocks, as a diff since the previous repo state.",
  "maxLength": 1000000
}
        """.trimIndent()
        val parsed = LexiconBytes(description = "CAR file containing relevant blocks, as a diff since the previous repo state.", maxLength = 1000000)
        assertEquals(parsed, json.decodeFromString(raw))
    }
}