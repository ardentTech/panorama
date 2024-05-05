package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#null
@Serializable
data class LexiconNull(
    override val description: String? = null
): SchemaDef.FieldType {
    override val type = LexiconType.NULL
}