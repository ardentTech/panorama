package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#token
@Serializable
data class LexiconToken(
    override val description: String? = null
): SchemaDef.Meta {
    override val type = LexiconType.TOKEN
}