package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconQuery(
    override val description: String? = null,
    val errors: List<Error>? = null,
    val output: IO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.Primary {
    override val type = LexiconType.QUERY

    @Serializable
    data class Error(
        override val description: String? = null,
        override val name: String
    ): PrimaryError

    @Serializable
    data class IO(
        override val description: String? = null,
        override val encoding: String,
        @Serializable(with = PrimaryIOSchemaSerializer::class) override val schema: PrimaryIOSchema? = null
    ): PrimaryIO
}