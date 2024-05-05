package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#boolean
@Serializable
data class LexiconBoolean(
    val const: Boolean? = null,
    val default: Boolean? = null,
    override val description: String? = null
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {
    override val type = LexiconType.BOOLEAN
}