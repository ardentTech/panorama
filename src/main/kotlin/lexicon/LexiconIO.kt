package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

// TODO unit test
private object IOSchema: JsonTransformingSerializer<SchemaDef>(SchemaDef.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val serializerCls = when (val type = this["type"]!!.jsonPrimitive.content) {
                LexiconType.REF -> LexiconRef::class
                LexiconType.OBJECT -> LexiconObject::class
                LexiconType.UNION -> LexiconUnion::class
                else -> throw IllegalArgumentException("Unexpected type: $type")
            }
            this["discriminator"] = JsonPrimitive("lexicon.${serializerCls.simpleName}")
        })
    }
}

@Serializable
data class LexiconIO(
    val description: String? = null,
    val encoding: String,
    @Serializable(with = IOSchema::class) val schema: SchemaDef? = null
)