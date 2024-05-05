package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconArrayTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "ref",
    "ref": "#repoOp",
    "description": "List of repo mutation operations in this commit (eg, records created, updated, or deleted)."
  },
  "maxLength": 200
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconRef(
                description = "List of repo mutation operations in this commit (eg, records created, updated, or deleted).",
                ref = "#repoOp"
            ),
            maxLength = 200
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}