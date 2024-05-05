package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconError(
    val description: String? = null,
    val name: String
)