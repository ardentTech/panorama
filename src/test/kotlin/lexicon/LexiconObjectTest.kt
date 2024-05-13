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
        // TODO
    }

    @Test
    fun `deserialize boolean property ok`() {
        // TODO
    }

    @Test
    fun `deserialize bytes property ok`() {
        // TODO
    }

    @Test
    fun `deserialize cid-link property ok`() {
        // TODO
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
        // TODO
    }

    @Test
    fun `deserialize string property ok`() {
        // TODO
    }

    @Test
    fun `deserialize unexpected property`() {
        // TODO
    }

    @Test
    fun `deserialize union property ok`() {
        // TODO
    }

    @Test
    fun `deserialize unknown property ok`() {
        // TODO
    }

    @Test
    fun `nullable items must be property keys`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconObject.factory(nullable = listOf("invalid"))
        }
    }

    @Test
    fun `nullable items ok`() {
        val key = "foo"
        LexiconObject.factory(
            nullable = listOf(key),
            properties = mapOf(key to LexiconString())
        )
    }

    @Test
    fun `required items must be property keys`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconObject.factory(required = listOf("invalid"))
        }
    }

    @Test
    fun `required items ok`() {
        val key = "bar"
        LexiconObject.factory(
            properties = mapOf(key to LexiconString()),
            required = listOf(key),
        )
    }
}