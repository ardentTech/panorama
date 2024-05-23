package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconParamsTest: LexiconTest() {

    @Test
    fun `deserialize array property ok`() {
        val raw = """
{
  "type": "params",
  "properties": {
    "limit": {
      "type": "array",
      "items": {
        "type": "blob"
      }
    }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "limit" to LexiconArray(
                    items = LexiconBlob()
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize boolean property ok`() {
        val raw = """
{
  "type": "params",
  "properties": {
    "limit": {
      "type": "boolean"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "limit" to LexiconBoolean()
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize integer property ok`() {
        val raw = """
{
  "type": "params",
  "properties": {
    "limit": {
      "type": "integer",
      "minimum": 1,
      "maximum": 100,
      "default": 50
    }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "limit" to LexiconInteger(
                    default = 50,
                    maximum = 100,
                    minimum = 1,
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize string property ok`() {
        val raw = """
{
  "type": "params",
  "properties": {
    "feed": { "type": "string", "format": "at-uri" }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "feed" to LexiconString(format = LexiconString.Format.AT_URI)
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unexpected property`() {
        assertFailsWith<IllegalArgumentException> {
            val raw = """
{
  "type": "params",
  "properties": {
    "limit": {
      "type": "invalid"
    }
  }
}
        """.trimIndent()
            json.decodeFromString<LexiconParams>(raw)
        }
    }

    @Test
    fun `deserialize unknown property ok`() {
        val raw = """
{
  "type": "params",
  "properties": {
    "limit": {
      "type": "unknown"
    }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "limit" to LexiconUnknown()
            )
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