package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#bytes
@Serializable
data class LexiconBytes(
    override val description: String? = null,
    val maxLength: Int? = null,
    val minLength: Int? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.BYTES
}