package lexicon

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconStringTest: LexiconTest() {

    @Test
    fun `deserialize ok`() {
        val raw = """
{
  "type": "string"
}            
        """.trimIndent()
        val parsed = LexiconString()
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format at-identifier ok`() {
        val raw = """
{
  "type": "string",
  "format": "at-identifier"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.AT_IDENTIFIER)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format at-uri ok`() {
        val raw = """
{
  "type": "string",
  "format": "at-uri"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.AT_URI)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format cid ok`() {
        val raw = """
{
  "type": "string",
  "format": "cid"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.CID)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format datetime ok`() {
        val raw = """
{
  "type": "string",
  "format": "datetime"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.DATETIME)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format did ok`() {
        val raw = """
{
  "type": "string",
  "format": "did"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.DID)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format handle ok`() {
        val raw = """
{
  "type": "string",
  "format": "handle"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.HANDLE)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format language ok`() {
        val raw = """
{
  "type": "string",
  "format": "language"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.LANGUAGE)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format nsid ok`() {
        val raw = """
{
  "type": "string",
  "format": "nsid"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.NSID)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format record-key ok`() {
        val raw = """
{
  "type": "string",
  "format": "record-key"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.RECORD_KEY)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format tid ok`() {
        val raw = """
{
  "type": "string",
  "format": "tid"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.TID)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format uri ok`() {
        val raw = """
{
  "type": "string",
  "format": "uri"
}            
        """.trimIndent()
        val parsed = LexiconString(format = LexiconString.Format.URI)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize format unexpected`() {
        assertFailsWith<IllegalArgumentException> {
            val raw = """
{
  "type": "string",
  "format": "invalid"
}            
        """.trimIndent()
            json.decodeFromString(raw)
        }
    }
}