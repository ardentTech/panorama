package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

private object LexiconDocDefs: JsonTransformingSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())) {
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
                this["discriminator"] = JsonPrimitive("lexicon.${serializerCls.simpleName}")
            })
        }
        return JsonObject(content = content)
    }
}

@Serializable
data class LexiconDoc(
    @Serializable(with = LexiconDocDefs::class) val defs: Map<String, SchemaDef>,
    val description: String? = null,
    val id: String,
    val lexicon: Int,
    val revision: Int? = null,
) {
    init {
        require(defs.isNotEmpty())

        var primaryDefs = 0
        defs.forEach {
            if (it.value is SchemaDef.PrimaryType) {
                primaryDefs++
                if (primaryDefs > 1) { throw IllegalArgumentException("Can have at most one primary definition") }
            }
        }
    }
}