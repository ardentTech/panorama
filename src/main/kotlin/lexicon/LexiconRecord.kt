package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconRecord(
    override val description: String? = null,
    val key: String,
    val record: LexiconObject
): SchemaDef.Primary {
    override val type = LexiconType.RECORD
}