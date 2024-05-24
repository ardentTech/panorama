package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#integer
@Serializable
data class LexiconInteger(
    val const: Int? = null,
    val default: Int? = null,
    override val description: String? = null,
    val enum: List<Int>? = null,
    val maximum: Int? = null,
    val minimum: Int? = null,
): SchemaDef.Concrete {
    override val type = LexiconType.INTEGER
}