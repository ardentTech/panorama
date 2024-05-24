package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#unknown
@Serializable
data class LexiconUnknown(
    override val description: String? = null
): SchemaDef.Meta {
    override val type = LexiconType.UNKNOWN
}