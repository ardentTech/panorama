package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconSubscriptionTest: LexiconTest() {

    @Test
    fun `deserialize ok `() {
        val raw = """
{
  "type": "subscription",
  "description": "Subscribe to stream of labels (and negations). Public endpoint implemented by mod services. Uses same sequencing scheme as repo event stream.",
  "parameters": {
    "type": "params",
    "properties": {
      "cursor": {
        "type": "integer",
        "description": "The last known event seq number to backfill from."
      }
    }
  },
  "message": {
    "schema": {
      "type": "union",
      "refs": ["#labels", "#info"]
    }
  },
  "errors": [{ "name": "FutureCursor" }]
}            
        """.trimIndent()
        val parsed = LexiconSubscription(
            description = "Subscribe to stream of labels (and negations). Public endpoint implemented by mod services. Uses same sequencing scheme as repo event stream.",
            errors = listOf(LexiconSubscription.Error(name = "FutureCursor")),
            message = LexiconSubscription.Message(schema = LexiconUnion(refs = listOf("#labels", "#info"))),
            parameters = LexiconParams(
                properties = mapOf("cursor" to LexiconInteger(description = "The last known event seq number to backfill from."))
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}