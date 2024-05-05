package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconUnionTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "union",
  "refs": ["#listPurpose"]
}    
        """.trimIndent()
        val parsed = LexiconUnion(refs = listOf("#listPurpose"))
        assertEquals(parsed, json.decodeFromString(raw))
    }
}