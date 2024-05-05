package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconRecordTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
    {
      "type": "record",
      "description": "Record declaring a 'like' of a piece of subject content.",
      "key": "tid",
      "record": {
        "type": "object",
        "required": ["subject", "createdAt"],
        "properties": {
          "subject": { "type": "ref", "ref": "com.atproto.repo.strongRef" },
          "createdAt": { "type": "string", "format": "datetime" }
        }
      }
    }            
        """.trimIndent()
        val parsed = LexiconRecord(
            description = "Record declaring a 'like' of a piece of subject content.",
            key = "tid",
            record = LexiconObject(
                required = listOf("subject", "createdAt"),
                properties = mapOf(
                    "subject" to LexiconRef(ref = "com.atproto.repo.strongRef"),
                    "createdAt" to LexiconString(format = LexiconString.Format.DATETIME)
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}