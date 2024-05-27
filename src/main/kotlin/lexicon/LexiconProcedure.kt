package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconProcedure(
    override val description: String? = null,
    val errors: List<Error>? = null,
    val input: IO? = null,
    val output: IO? = null,
    val parameters: LexiconParams? = null,
): SchemaDef.Primary {
    override val type = LexiconType.PROCEDURE

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