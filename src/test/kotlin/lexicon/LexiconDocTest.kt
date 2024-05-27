package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconDocTest: LexiconTest() {

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
    fun `deserialize array defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize blob defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize boolean defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize bytes defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize cid-link defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize integer defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize object defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize procedure defs ok`() {
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
                    input = LexiconProcedure.IO(
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
    fun `deserialize query defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize record defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize ref defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize string defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize subscription defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize unexpected defs`() {
        // TODO
    }

    @Test
    fun `deserialize union defs ok`() {
        // TODO
    }

    @Test
    fun `deserialize unknown defs ok`() {
        // TODO
    }

    @Test
    fun `must have at least one definition`() {
        assertFailsWith<IllegalArgumentException> {
            LexiconDoc.factory(
                defs = mapOf()
            )
        }
    }

    @Test
    fun `name ok`() {
        val id = "foo.bar.testing.lexiconName"
        val lex = LexiconDoc.factory(
            defs = mapOf("one" to LexiconQuery()),
            id = id
        )
        assertEquals(id.split(".").last(), lex.name)
    }

    @Test
    fun `namespace ok`() {
        val id = "foo.bar.testing.lexiconName"
        val lex = LexiconDoc.factory(
            defs = mapOf("one" to LexiconQuery()),
            id = id
        )
        assertEquals(id.split(".").dropLast(1).joinToString("."), lex.namespace)
    }
}