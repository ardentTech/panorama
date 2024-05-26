package lexicon

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

private object LexiconProcedureIOSchema: JsonTransformingSerializer<LexiconProcedure.IO.Schema>(LexiconProcedure.IO.Schema.serializer()) {
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
data class LexiconProcedure(
    override val description: String? = null,
    val errors: List<Error>? = null,
    val input: IO? = null,
    val output: IO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.Primary {
    override val type = LexiconType.PROCEDURE

    @Serializable
    data class Error(
        val description: String? = null,
        val name: String
    )

    @Serializable
    data class IO(
        val description: String? = null,
        val encoding: String,
        @Serializable(with = LexiconProcedureIOSchema::class) val schema: Schema? = null
    ) {
        @Serializable
        sealed interface Schema
    }
}