package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

// TODO unit test
private object ObjectProperties: JsonTransformingSerializer<Map<String, LexiconObject.Property>>(MapSerializer(String.serializer(), LexiconObject.Property.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val serializerCls = when (val type = this["type"]!!.jsonPrimitive.content) {
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
                this["discriminator"] = JsonPrimitive("lexicon.${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}

// https://atproto.com/specs/lexicon#object
@Serializable
data class LexiconObject(
    override val description: String? = null,
    val nullable: List<String>? = null,
    @Serializable(with = ObjectProperties::class) val properties: Map<String, Property>,
    val required: List<String>? = null,
): SchemaDef.Container, LexiconIO.Schema {

    @Serializable
    sealed interface Property

    override val type = LexiconType.OBJECT

    init {
        nullable?.forEach { require(it in properties.keys) }
        required?.forEach { require(it in properties.keys) }
    }
}