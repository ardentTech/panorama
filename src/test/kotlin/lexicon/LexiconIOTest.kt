package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconIOTest: LexiconTest() {

    @Test
    fun `deserialize ref schema ok`() {
        val raw = """
{
  "encoding": "application/json",
  "schema": {
    "type": "ref",
    "ref": "com.atproto.admin.defs#accountView"
  }
}
        """.trimIndent()
        val parsed = LexiconIO(
            encoding = "application/json",
            schema = LexiconRef(ref = "com.atproto.admin.defs#accountView")
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize object schema ok`() {
        val raw = """
{
  "encoding": "application/json",
  "schema": {
    "type": "object",
    "required": ["email", "token"],
    "properties": {
      "email": { "type": "string" },
      "token": { "type": "string" }
    }
  }
}
        """.trimIndent()
        val parsed = LexiconIO(
            encoding = "application/json",
            schema = LexiconObject(
                required = listOf("email", "token"),
                properties = mapOf(
                    "email" to LexiconString(),
                    "token" to LexiconString()
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unexpected schema type`() {
        assertFailsWith<IllegalArgumentException> {
            val raw = """
{
  "encoding": "application/json",
  "schema": {
    "type": "boolean"
  }
}
        """.trimIndent()
            json.decodeFromString<LexiconIO>(raw)
        }
    }

    @Test
    fun `deserialize union schema ok`() {
        val raw = """
{
  "encoding": "application/json",
  "schema": {
    "type": "union",
    "refs": [
      "chat.bsky.convo.defs#logBeginConvo",
      "chat.bsky.convo.defs#logLeaveConvo"
    ]
  }
}
        """.trimIndent()
        val parsed = LexiconIO(
            encoding = "application/json",
            schema = LexiconUnion(refs = listOf("chat.bsky.convo.defs#logBeginConvo", "chat.bsky.convo.defs#logLeaveConvo"))
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}