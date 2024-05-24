package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

// TODO unit test
private object ParamsProperties: JsonTransformingSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val serializerCls = when (val type = this["type"]!!.jsonPrimitive.content) {
                    LexiconType.ARRAY -> LexiconArray::class
                    LexiconType.BOOLEAN -> LexiconBoolean::class
                    LexiconType.INTEGER -> LexiconInteger::class
                    LexiconType.STRING -> LexiconString::class
                    LexiconType.UNKNOWN -> LexiconUnknown::class
                    else -> throw IllegalArgumentException("Unexpected type: $type")
                }
                this["discriminator"] = JsonPrimitive("lexicon.${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}

// https://atproto.com/specs/lexicon#params
@Serializable
data class LexiconParams(
    override val description: String? = null,
    @Serializable(with = ParamsProperties::class) val properties: Map<String, SchemaDef>,
    val required: List<String>? = null,
): SchemaDef.Container {
    override val type = LexiconType.PARAMS

    init {
        required?.forEach { require(it in properties.keys) }
    }
}