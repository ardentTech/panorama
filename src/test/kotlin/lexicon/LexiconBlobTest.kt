package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconBlobTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "blob",
  "accept": ["image/png", "image/jpeg"],
  "maxSize": 1000000
} 
        """.trimIndent()
        val parsed = LexiconBlob(accept = listOf("image/png", "image/jpeg"), maxSize = 1000000)
        assertEquals(parsed, json.decodeFromString(raw))
    }
}