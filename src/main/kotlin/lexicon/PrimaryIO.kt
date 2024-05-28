package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

interface PrimaryIO {
    val description: String?
    val encoding: String
    val schema: PrimaryIOSchema?
}

@Serializable
sealed interface PrimaryIOSchema {}

object PrimaryIOSchemaSerializer: JsonTransformingSerializer<PrimaryIOSchema>(PrimaryIOSchema.serializer()) {
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