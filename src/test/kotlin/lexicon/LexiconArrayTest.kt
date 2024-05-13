package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconArrayTest: LexiconTest() {

    @Test
    fun `deserialize blob items ok`() {
        // TODO
    }

    @Test
    fun `deserialize boolean items ok`() {
        // TODO
    }

    @Test
    fun `deserialize bytes items ok`() {
        // TODO
    }

    @Test
    fun `deserialize cid-link items ok`() {
        // TODO
    }

    @Test
    fun `deserialize integer items ok`() {
        // TODO
    }

    @Test
    fun `deserialize ref items ok`() {
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

    @Test
    fun `deserialize string items ok`() {
        // TODO
    }

    @Test
    fun `deserialize unexpected items`() {
        // TODO
    }

    @Test
    fun `deserialize union items ok`() {
        // TODO
    }

    @Test
    fun `deserialize unknown items ok`() {
        // TODO
    }
}