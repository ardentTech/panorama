package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#ref
@Serializable
data class LexiconRef(
    override val description: String? = null,
    val ref: String
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconIO.Schema {
    override val type = LexiconType.REF
}