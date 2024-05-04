import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconTest {

    private val json by lazy {
        Json {
            classDiscriminator = "discriminator"
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }

    @Test
    fun `deserialize array ok`() {
        val raw = """
{
  "type": "array",
  "items": {
    "type": "ref",
    "ref": "#repoOp",
    "description": "List of repo mutation operations in this commit (eg, records created, updated, or deleted)."
  },
  "maxLength": 200
}
        """.trimIndent()
        val parsed = LexiconArray(
            items = LexiconRef(
                description = "List of repo mutation operations in this commit (eg, records created, updated, or deleted).",
                ref = "#repoOp"
            ),
            maxLength = 200
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize boolean ok`() {
        val raw = """
{
  "type": "boolean",
  "const": true
}    
        """.trimIndent()
        val parsed = LexiconBoolean(const = true)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize blob ok`() {
        val raw = """
{
  "type": "blob",
  "accept": ["image/png", "image/jpeg"],
  "maxSize": 1000000
} 
        """.trimIndent()
        val parsed = LexiconBlob(accept = listOf("image/png", "image/jpeg"), maxSize = 1000000)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize bytes ok`() {
        val raw = """
{
  "type": "bytes",
  "description": "CAR file containing relevant blocks, as a diff since the previous repo state.",
  "maxLength": 1000000
}
        """.trimIndent()
        val parsed = LexiconBytes(description = "CAR file containing relevant blocks, as a diff since the previous repo state.", maxLength = 1000000)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize cid-link ok`() {
        val raw = """
{
  "type": "cid-link"
}    
        """.trimIndent()
        val parsed = LexiconCidLink()
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize error ok`() {
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

    @Test
    fun `deserialize integer ok`() {
        val raw = """
{
  "type": "integer",
  "minimum": 1,
  "maximum": 100,
  "default": 50
}  
        """.trimIndent()
        val parsed = LexiconInteger(default = 50, maximum = 100, minimum = 1)
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize io schema ok`() {
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
    fun `deserialize lexicon doc ok`() {
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
        val parsed = LexiconDoc(
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
    fun `deserialize null ok`() {
        val raw = """
{
  "type": "null"
}    
        """.trimIndent()
        val parsed = LexiconNull()
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize object ok `() {
        val raw = """
{
  "type": "object",
  "required": ["seq", "labels"],
  "properties": {
    "seq": { "type": "integer" },
    "labels": {
      "type": "array",
      "items": { "type": "ref", "ref": "com.atproto.label.defs#label" }
    }
  }
}
        """.trimIndent()
        val parsed = LexiconObject(
            required = listOf("seq", "labels"),
            properties = mapOf(
                "seq" to LexiconInteger(),
                "labels" to LexiconArray(
                    items = LexiconRef(ref = "com.atproto.label.defs#label")
                )
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize params ok`() {
        val raw = """
{
  "type": "params",
  "required": ["feed"],
  "properties": {
    "feed": { "type": "string", "format": "at-uri" },
    "limit": {
      "type": "integer",
      "minimum": 1,
      "maximum": 100,
      "default": 50
    },
    "cursor": { "type": "string" }
  }
}
        """.trimIndent()
        val parsed = LexiconParams(
            properties = mapOf(
                "feed" to LexiconString(format = LexiconString.Format.AT_URI),
                "limit" to LexiconInteger(
                    default = 50,
                    maximum = 100,
                    minimum = 1,
                ),
                "cursor" to LexiconString()
            ),
            required = listOf("feed")
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize procedure ok `() {
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

    @Test
    fun `deserialize query ok `() {
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

    @Test
    fun `deserialize record ok `() {
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

    @Test
    fun `deserialize ref ok`() {
        val raw = """
{
  "type": "ref",
  "ref": "#listPurpose"
}    
        """.trimIndent()
        val parsed = LexiconRef(ref = "#listPurpose")
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize string ok`() {
        val raw = """
{
  "type": "string"
}            
        """.trimIndent()
        val parsed = LexiconString()
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize string format at-identifier ok`() {
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
    fun `deserialize string format at-uri ok`() {
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
    fun `deserialize string format cid ok`() {
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
    fun `deserialize string format datetime ok`() {
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
    fun `deserialize string format did ok`() {
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
    fun `deserialize string format handle ok`() {
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
    fun `deserialize string format language ok`() {
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
    fun `deserialize string format nsid ok`() {
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
    fun `deserialize string format record-key ok`() {
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
    fun `deserialize string format tid ok`() {
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
    fun `deserialize string format uri ok`() {
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
    fun `deserialize string format unexpected`() {
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

    @Test
    fun `deserialize subscription ok `() {
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
            errors = listOf(LexiconError(name = "FutureCursor")),
            message = LexiconSubscription.Message(schema = LexiconUnion(refs = listOf("#labels", "#info"))),
            parameters = LexiconParams(
                properties = mapOf("cursor" to LexiconInteger(description = "The last known event seq number to backfill from."))
            )
        )
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize token ok`() {
        val raw = """
{
  "type": "token",
  "description": "User clicked through to the feed item"
}            
        """.trimIndent()
        val parsed = LexiconToken(description = "User clicked through to the feed item")
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize union ok`() {
        val raw = """
{
  "type": "union",
  "refs": ["#listPurpose"]
}    
        """.trimIndent()
        val parsed = LexiconUnion(refs = listOf("#listPurpose"))
        assertEquals(parsed, json.decodeFromString(raw))
    }

    @Test
    fun `deserialize unknown ok`() {
        val raw = """
{
  "type": "unknown",
  "description": "The complete DID document for this account."
}   
        """.trimIndent()
        val parsed = LexiconUnknown(description = "The complete DID document for this account.")
        assertEquals(parsed, json.decodeFromString(raw))
    }
}