package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconIOTest: LexiconTest() {

    @Test
    fun `deserialize ref schema ok`() {
        // TODO
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
        // TODO
    }

    @Test
    fun `deserialize union schema ok`() {
        // TODO
    }
}