package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconQueryTest: LexiconTest() {

    @Test
    fun `deserialize ok `() {
        val raw = """
{
  "type": "query",
  "description": "Get information about the current auth session. Requires auth.",
  "output": {
    "encoding": "application/json",
    "schema": {
      "type": "object",
      "required": ["handle", "did"],
      "properties": {
        "handle": { "type": "string", "format": "handle" },
        "did": { "type": "string", "format": "did" },
        "email": { "type": "string" },
        "emailConfirmed": { "type": "boolean" },
        "emailAuthFactor": { "type": "boolean" },
        "didDoc": { "type": "unknown" }
      }
    }
  }
}           
        """.trimIndent()
        val parsed = LexiconQuery(
            description = "Get information about the current auth session. Requires auth.",
            output = LexiconIO(
                encoding = "application/json",
                schema = LexiconObject(
                    required = listOf("handle", "did"),
                    properties = mapOf(
                        "handle" to LexiconString(format = LexiconString.Format.HANDLE),
                        "did" to LexiconString(format = LexiconString.Format.DID),
                        "email" to LexiconString(),
                        "emailConfirmed" to LexiconBoolean(),
                        "emailAuthFactor" to LexiconBoolean(),
                        "didDoc" to LexiconUnknown()
                    )
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}