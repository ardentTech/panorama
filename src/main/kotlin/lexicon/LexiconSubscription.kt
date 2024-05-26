package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconSubscription(
    override val description: String? = null,
    val errors: List<Error>? = null,
    val message: Message? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.Primary {

    @Serializable
    data class Error(
        val description: String? = null,
        val name: String
    )

    @Serializable
    data class Message(
        val description: String? = null,
        val schema: LexiconUnion
    )

    override val type = LexiconType.SUBSCRIPTION
}