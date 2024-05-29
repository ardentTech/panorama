package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconObjectTest: LexiconTest() {

    @Test
    fun `deserialize array property ok `() {
        val raw = """
{
  "type": "object",
  "required": ["labels"],
  "properties": {
    "labels": {
      "type": "array",
      "items": { "type": "ref", "ref": "com.atproto.label.defs#label" }
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            required = listOf("labels"),
            properties = mapOf(
                "labels" to LexiconArray(
                    items = LexiconRef(ref = "com.atproto.label.defs#label")
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize blob property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "blob"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconBlob())
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize boolean property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "boolean"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconBoolean())
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize bytes property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "bytes"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconBytes())
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize cid-link property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "cid-link"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconCidLink())
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize integer property ok`() {
        val raw = """
{
  "type": "object",
  "required": ["seq"],
  "properties": {
    "seq": { "type": "integer" }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            required = listOf("seq"),
            properties = mapOf(
                "seq" to LexiconInteger(),
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize ref property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "ref",
      "ref": "chat.bsky.convo.defs#logBeginConvo"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconRef(ref = "chat.bsky.convo.defs#logBeginConvo"))
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize string property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "string"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconString())
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unexpected property`() {
        // TODO
    }

    @Test
    fun `deserialize union property ok`() {
        val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "union",
      "refs": [
        "chat.bsky.convo.defs#logBeginConvo",
        "chat.bsky.convo.defs#logLeaveConvo"
      ]
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            properties = mapOf("foobar" to LexiconUnion(
                refs = listOf(
                    "chat.bsky.convo.defs#logBeginConvo",
                    "chat.bsky.convo.defs#logLeaveConvo"
                )
            ))
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unknown property ok`() {
        assertFailsWith<IllegalArgumentException> {
            val raw = """
{
  "type": "object",
  "properties": {
    "foobar": {
      "type": "invalid"
    }
  }
}
            """.trimIndent()
            json.decodeFromString<LexiconObject>(raw)
        }
    }

    @Test
    fun `nullable items must be property keys`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconObject.fake(nullable = listOf("invalid"))
        }
    }

    @Test
    fun `nullable items ok`() {
        val key = "foo"
        LexiconObject.fake(
            nullable = listOf(key),
            properties = mapOf(key to LexiconString())
        )
    }

    @Test
    fun `required items must be property keys`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconObject.fake(required = listOf("invalid"))
        }
    }

    @Test
    fun `required items ok`() {
        val key = "bar"
        LexiconObject.fake(
            properties = mapOf(key to LexiconString()),
            required = listOf(key),
        )
    }
}