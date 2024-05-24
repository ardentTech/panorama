package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

// TODO unit test
private object ArrayItems: JsonTransformingSerializer<SchemaDef>(SchemaDef.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val serializerCls = when (val type = this["type"]!!.jsonPrimitive.content) {
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
}

// https://atproto.com/specs/lexicon#array
@Serializable
data class LexiconArray(
    override val description: String? = null,
    @Serializable(with = ArrayItems::class) val items: SchemaDef,
    val maxLength: Int? = null,
    val minLength: Int? = null,
): SchemaDef.Container {
    override val type = LexiconType.ARRAY
}