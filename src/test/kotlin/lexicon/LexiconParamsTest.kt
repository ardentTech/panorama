package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconParamsTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "params",
  "required": ["feed"],
  "properties": {
    "feed": { "type": "string", "format": "at-uri" },
    "limit": {
      "type": "integer",
      "minimum": 1,
      "maximum": 100,
      "default": 50
    },
    "cursor": { "type": "string" }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "feed" to LexiconString(format = LexiconString.Format.AT_URI),
                "limit" to LexiconInteger(
                    default = 50,
                    maximum = 100,
                    minimum = 1,
                ),
                "cursor" to LexiconString()
            ),
            required = listOf("feed")
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `required items must be property keys`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconParams.factory(
                required = listOf("invalid")
            )
        }
    }

    @Test
    fun `required items ok`() {
        val key = "foo"
        LexiconParams.factory(
            properties = mapOf(key to LexiconString()),
            required = listOf(key)
        )
    }
}