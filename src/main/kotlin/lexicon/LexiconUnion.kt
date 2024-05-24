package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#union
@Serializable
data class LexiconUnion(
    val closed: Boolean? = null,
    override val description: String? = null,
    val refs: List<String>
): SchemaDef.Meta {
    override val type = LexiconType.UNION
}