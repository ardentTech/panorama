package lexicon

import kotlinx.serialization.Serializable

@Serializable
data class LexiconRecord(
    override val description: String? = null,
    val key: String,
    val record: LexiconObject
): SchemaDef.PrimaryType {
    override val type = LexiconType.RECORD
}