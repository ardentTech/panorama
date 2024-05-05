package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconDocTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "lexicon": 1,
  "id": "com.atproto.admin.deleteAccount",
  "defs": {
    "main": {
      "type": "procedure",
      "description": "Delete a user account as an administrator.",
      "input": {
        "encoding": "application/json",
        "schema": {
          "type": "object",
          "required": ["did"],
          "properties": {
            "did": { "type": "string", "format": "did" }
          }
        }
      }
    }
  }
}
            
        """.trimIndent()
        val parsed = LexiconDoc.factory(
            id = "com.atproto.admin.deleteAccount",
            lexicon = 1,
            defs = mapOf(
                "main" to LexiconProcedure(
                    description = "Delete a user account as an administrator.",
                    input = LexiconIO(
                        encoding = "application/json",
                        schema = LexiconObject(
                            required = listOf("did"),
                            properties = mapOf(
                                "did" to LexiconString(format = LexiconString.Format.DID)
                            )
                        )
                    )
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `can have at most one primary definition`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconDoc.factory(
                defs = mapOf(
                    "one" to LexiconQuery(),
                    "two" to LexiconQuery()
                )
            )
        }
    }

    @Test
    fun `must have at least one definition`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconDoc.factory(
                defs = mapOf()
            )
        }
    }
}