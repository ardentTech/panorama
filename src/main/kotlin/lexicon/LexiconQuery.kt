package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconQuery(
    override val description: String? = null,
    val errors: List<LexiconError>? = null,
    val output: LexiconIO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.PrimaryType {
    override val type = LexiconType.QUERY
}