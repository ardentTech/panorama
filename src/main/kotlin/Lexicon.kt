import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object LexiconType {
    const val ARRAY = "array"
    const val BLOB = "blob"
    const val BOOLEAN = "boolean"
    const val BYTES = "bytes"
    const val CID_LINK = "cid-link"
    const val INTEGER = "integer"
    const val NULL = "null"
    const val OBJECT = "object"
    const val PARAMS = "params"
    const val PROCEDURE = "procedure"
    const val QUERY = "query"
    const val RECORD = "record"
    const val REF = "ref"
    const val STRING = "string"
    const val SUBSCRIPTION = "subscription"
    const val TOKEN = "token"
    const val UNION = "union"
    const val UNKNOWN = "unknown"
}

// "The schema definition language for atproto is Lexicon."
@Serializable
sealed interface SchemaDef {
    val description: String?
    val type: String

    sealed interface PrimaryType: SchemaDef
    sealed interface FieldType: SchemaDef
}

@Serializable
data class LexiconDoc(
    @Serializable(with = LexiconDocDefs::class) val defs: Map<String, SchemaDef>,
    val description: String? = null,
    val id: String,
    val lexicon: Int,
    val revision: Int? = null,
)

internal object LexiconDocDefs: JsonTransformingSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                val serializerCls = when (type) {
                    LexiconType.ARRAY -> LexiconArray::class
                    LexiconType.BLOB -> LexiconBlob::class
                    LexiconType.BOOLEAN -> LexiconBoolean::class
                    LexiconType.BYTES -> LexiconBytes::class
                    LexiconType.CID_LINK -> LexiconCidLink::class
                    LexiconType.INTEGER -> LexiconInteger::class
                    LexiconType.PROCEDURE -> LexiconProcedure::class
                    LexiconType.QUERY -> LexiconQuery::class
                    LexiconType.RECORD -> LexiconRecord::class
                    LexiconType.REF -> LexiconRef::class
                    LexiconType.STRING -> LexiconString::class
                    LexiconType.SUBSCRIPTION -> LexiconSubscription::class
                    LexiconType.UNION -> LexiconUnion::class
                    LexiconType.UNKNOWN -> LexiconUnknown::class
                    else -> throw IllegalArgumentException("Unexpected type: $type")
                }
                this["discriminator"] = JsonPrimitive("${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}


        @Serializable
data class LexiconError(
    val description: String? = null,
    val name: String
)

@Serializable
data class LexiconIO(
    val description: String? = null,
    val encoding: String,
    @Serializable(with = IOSchema::class) val schema: Schema? = null
) {
    // TODO add documentation pointing ide users to what implements this? same for others?
    @Serializable
    sealed interface Schema
}

// TODO unit test
internal object IOSchema: JsonTransformingSerializer<LexiconIO.Schema>(LexiconIO.Schema.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val type = this["type"]!!.jsonPrimitive.content
            val serializerCls = when (type) {
                LexiconType.REF -> LexiconRef::class
                LexiconType.OBJECT -> LexiconObject::class
                LexiconType.UNION -> LexiconUnion::class
                else -> throw IllegalArgumentException("Unexpected type: $type")
            }
            this["discriminator"] = JsonPrimitive("${serializerCls.simpleName}")
        })
    }
}

// PRIMARY TYPES
// https://atproto.com/specs/lexicon#primary-type-definitions

@Serializable
data class LexiconProcedure(
    override val description: String? = null,
    val errors: List<LexiconError>? = null,
    val input: LexiconIO? = null,
    val output: LexiconIO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.PrimaryType {
    override val type = LexiconType.PROCEDURE
}

@Serializable
data class LexiconQuery(
    override val description: String? = null,
    val errors: List<LexiconError>? = null,
    val output: LexiconIO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.PrimaryType {
    override val type = LexiconType.QUERY
}

@Serializable
data class LexiconRecord(
    override val description: String? = null,
    val key: String,
    val record: LexiconObject
): SchemaDef.PrimaryType {
    override val type = LexiconType.RECORD
}

@Serializable
data class LexiconSubscription(
    override val description: String? = null,
    val errors: List<LexiconError>? = null,
    val message: Message? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.PrimaryType {

    @Serializable
    data class Message(
        val description: String? = null,
        val schema: LexiconUnion
    )

    override val type = LexiconType.SUBSCRIPTION
}

// FIELD TYPES
// https://atproto.com/specs/lexicon#field-type-definitions

// https://atproto.com/specs/lexicon#array
@Serializable
data class LexiconArray(
    override val description: String? = null,
    @Serializable(with = ArrayItems::class) val items: Items,
    val maxLength: Int? = null,
    val minLength: Int? = null,
): SchemaDef.FieldType, LexiconObject.Property, LexiconParams.Property {

    @Serializable
    sealed interface Items

    override val type = LexiconType.ARRAY
}

// TODO unit test
internal object ArrayItems: JsonTransformingSerializer<LexiconArray.Items>(LexiconArray.Items.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val type = this["type"]!!.jsonPrimitive.content
            val serializerCls = when (type) {
                LexiconType.BLOB -> LexiconBlob::class
                LexiconType.BOOLEAN -> LexiconBoolean::class
                LexiconType.BYTES -> LexiconBytes::class
                LexiconType.CID_LINK -> LexiconCidLink::class
                LexiconType.INTEGER -> LexiconInteger::class
                LexiconType.REF -> LexiconRef::class
                LexiconType.STRING -> LexiconString::class
                LexiconType.UNION -> LexiconUnion::class
                LexiconType.UNKNOWN -> LexiconUnknown::class
                else -> throw IllegalArgumentException("Unexpected type: $type")
            }
            this["discriminator"] = JsonPrimitive("${serializerCls.simpleName}")
        })
    }
}

// https://atproto.com/specs/lexicon#boolean
@Serializable
data class LexiconBoolean(
    val const: Boolean? = null,
    val default: Boolean? = null,
    override val description: String? = null
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {
    override val type = LexiconType.BOOLEAN
}

// https://atproto.com/specs/lexicon#blob
@Serializable
data class LexiconBlob(
    // TODO restrict to mime types? https://www.iana.org/assignments/media-types/media-types.xhtml
    val accept: List<String>? = null,
    override val description: String? = null,
    val maxSize: Int? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.BLOB
}

// https://atproto.com/specs/lexicon#bytes
@Serializable
data class LexiconBytes(
    override val description: String? = null,
    val maxLength: Int? = null,
    val minLength: Int? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.BYTES
}

// https://atproto.com/specs/lexicon#cid-link
@Serializable
data class LexiconCidLink(
    override val description: String? = null
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.CID_LINK
}

// https://atproto.com/specs/lexicon#integer
@Serializable
data class LexiconInteger(
    val const: Int? = null,
    val default: Int? = null,
    override val description: String? = null,
    val enum: List<Int>? = null,
    val maximum: Int? = null,
    val minimum: Int? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {
    override val type = LexiconType.INTEGER
}

// https://atproto.com/specs/lexicon#null
@Serializable
data class LexiconNull(
    override val description: String? = null
): SchemaDef.FieldType {
    override val type = LexiconType.NULL
}

// https://atproto.com/specs/lexicon#object
@Serializable
data class LexiconObject(
    override val description: String? = null,
    val nullable: List<String>? = null,
    @Serializable(with = ObjectProperties::class) val properties: Map<String, Property>,
    val required: List<String>? = null,
): SchemaDef.FieldType, LexiconIO.Schema {

    @Serializable
    sealed interface Property

    override val type = LexiconType.OBJECT

    init {
        nullable?.forEach { require(it in properties.keys) }
        required?.forEach { require(it in properties.keys) }
    }
}

// TODO unit test
internal object ObjectProperties: JsonTransformingSerializer<Map<String, LexiconObject.Property>>(MapSerializer(String.serializer(), LexiconObject.Property.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                val serializerCls = when (type) {
                    LexiconType.ARRAY -> LexiconArray::class
                    LexiconType.BLOB -> LexiconBlob::class
                    LexiconType.BOOLEAN -> LexiconBoolean::class
                    LexiconType.BYTES -> LexiconBytes::class
                    LexiconType.CID_LINK -> LexiconCidLink::class
                    LexiconType.INTEGER -> LexiconInteger::class
                    LexiconType.REF -> LexiconRef::class
                    LexiconType.STRING -> LexiconString::class
                    LexiconType.UNION -> LexiconUnion::class
                    LexiconType.UNKNOWN -> LexiconUnknown::class
                    else -> throw IllegalArgumentException("Unexpected type: $type")
                }
                this["discriminator"] = JsonPrimitive("${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}

// https://atproto.com/specs/lexicon#params
@Serializable
data class LexiconParams(
    override val description: String? = null,
    @Serializable(with = ParamsProperties::class) val properties: Map<String, Property>,
    val required: List<String>? = null,
): SchemaDef.FieldType {
    @Serializable
    sealed interface Property

    override val type = LexiconType.PARAMS

    init {
        required?.forEach { require(it in properties.keys) }
    }
}


// TODO unit test
internal object ParamsProperties: JsonTransformingSerializer<Map<String, LexiconParams.Property>>(MapSerializer(String.serializer(), LexiconParams.Property.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                val serializerCls = when (type) {
                    LexiconType.ARRAY -> LexiconArray::class
                    LexiconType.BOOLEAN -> LexiconBoolean::class
                    LexiconType.INTEGER -> LexiconInteger::class
                    LexiconType.STRING -> LexiconString::class
                    LexiconType.UNKNOWN -> LexiconUnknown::class
                    else -> throw IllegalArgumentException("Unexpected type: $type")
                }
                this["discriminator"] = JsonPrimitive("${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}

// https://atproto.com/specs/lexicon#ref
@Serializable
data class LexiconRef(
    override val description: String? = null,
    val ref: String
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconIO.Schema {
    override val type = LexiconType.REF
}

@Serializable
data class LexiconString(
    val const: String? = null,
    val default: String? = null,
    override val description: String? = null,
    val enum: List<String>? = null,
    @Serializable(with = LexiconStringFormat::class) val format: Format? = null,
    val maxLength: Int? = null,
    val minLength: Int? = null,
    val maxGraphemes: Int? = null,
    val minGraphemes: Int? = null,
    val knownValues: List<String>? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {

    @Serializable
    enum class Format(val code: String) {
        AT_IDENTIFIER("at-identifier"),
        AT_URI("at-uri"),
        CID("cid"),
        DATETIME("datetime"),
        DID("did"),
        HANDLE("handle"),
        LANGUAGE("language"),
        NSID("nsid"),
        RECORD_KEY("record-key"),
        TID("tid"),
        URI("uri"),
    }

    override val type = LexiconType.STRING
}

// TODO unit test
internal object LexiconStringFormat: KSerializer<LexiconString.Format> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LexiconString.Format", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LexiconString.Format {
        return decoder.decodeString().let { code ->
            LexiconString.Format.entries.firstOrNull { it.code == code } ?: run {
                throw IllegalArgumentException("Unexpected format: $code")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: LexiconString.Format) = encoder.encodeString(value.code)
}

// https://atproto.com/specs/lexicon#token
@Serializable
data class LexiconToken(
    override val description: String? = null
): SchemaDef.FieldType {
    override val type = LexiconType.TOKEN
}

// https://atproto.com/specs/lexicon#union
@Serializable
data class LexiconUnion(
    val closed: Boolean? = null,
    override val description: String? = null,
    val refs: List<String>
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconIO.Schema {
    override val type = LexiconType.UNION
}

// https://atproto.com/specs/lexicon#unknown
@Serializable
data class LexiconUnknown(
    override val description: String? = null
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {
    override val type = LexiconType.UNKNOWN
}