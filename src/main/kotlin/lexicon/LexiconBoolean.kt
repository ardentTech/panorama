package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#boolean
@Serializable
data class LexiconBoolean(
    val const: Boolean? = null,
    val default: Boolean? = null,
    override val description: String? = null
): SchemaDef.Concrete {
    override val type = LexiconType.BOOLEAN
}