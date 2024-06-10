package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#bytes
@Serializable
data class LexiconBytes(
    override val description: String? = null,
    // maximum size of value, as raw bytes with no encoding
    val maxLength: Int? = null,
    // minimum size of value, as raw bytes with no encoding
    val minLength: Int? = null,
): SchemaDef.Concrete, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.BYTES
}