package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#token
@Serializable
data class LexiconToken(
    override val description: String? = null
): SchemaDef.FieldType {
    override val type = LexiconType.TOKEN
}