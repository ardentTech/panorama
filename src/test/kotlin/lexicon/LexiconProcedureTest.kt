package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconProcedureTest: LexiconTest() {

    @Test
    fun `deserialize ok `() {
        val raw = """
{
  "type": "procedure",
  "description": "Creates a mute relationship for the specified account. Mutes are private in Bluesky. Requires auth.",
  "input": {
    "encoding": "application/json",
    "schema": {
      "type": "object",
      "required": ["actor"],
      "properties": {
        "actor": { "type": "string", "format": "at-identifier" }
      }
    }
  }
}           
        """.trimIndent()
        val parsed = LexiconProcedure(
            description = "Creates a mute relationship for the specified account. Mutes are private in Bluesky. Requires auth.",
            input = LexiconIO(
                encoding = "application/json",
                schema = LexiconObject(
                    required = listOf("actor"),
                    properties = mapOf(
                        "actor" to LexiconString(format = LexiconString.Format.AT_IDENTIFIER)
                    )
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}