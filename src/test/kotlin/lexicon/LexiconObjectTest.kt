package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconObjectTest: LexiconTest() {

    @Test
    fun `deserialize ok `() {
        val raw = """
{
  "type": "object",
  "required": ["seq", "labels"],
  "properties": {
    "seq": { "type": "integer" },
    "labels": {
      "type": "array",
      "items": { "type": "ref", "ref": "com.atproto.label.defs#label" }
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            required = listOf("seq", "labels"),
            properties = mapOf(
                "seq" to LexiconInteger(),
                "labels" to LexiconArray(
                    items = LexiconRef(ref = "com.atproto.label.defs#label")
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
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