package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconArrayTest: LexiconTest() {

    @Test
    fun `deserialize blob items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "blob"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconBlob()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize boolean items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "boolean"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconBoolean()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize bytes items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "bytes"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconBytes()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize cid-link items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "cid-link"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconCidLink()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize integer items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "integer"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconInteger()
        )
        assertEquals(parsed, json.decodeFromString(raw))
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
        val raw = """
{
  "type": "array",
  "items": {
    "type": "string"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconString()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unexpected items`() {
        assertFailsWith<IllegalArgumentException> {
            val raw = """
{
  "type": "array",
  "items": {
    "type": "invalid"
  }
}
        """.trimIndent()
            json.decodeFromString(raw)
        }
    }

    @Test
    fun `deserialize union items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "union",
    "refs": [
      "chat.bsky.convo.defs#logBeginConvo",
      "chat.bsky.convo.defs#logLeaveConvo"
    ]
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconUnion(refs = listOf("chat.bsky.convo.defs#logBeginConvo", "chat.bsky.convo.defs#logLeaveConvo"))
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unknown items ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "unknown"
  }
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconUnknown()
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}