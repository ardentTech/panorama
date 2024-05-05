package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals

class LexiconErrorTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "name": "ConsumerTooSlow",
  "description": "If the consumer of the stream can not keep up with events, and a backlog gets too large, the server will drop the connection."
}
        """.trimIndent()
        val parsed = LexiconError(
            description = "If the consumer of the stream can not keep up with events, and a backlog gets too large, the server will drop the connection.",
            name = "ConsumerTooSlow"
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }
}