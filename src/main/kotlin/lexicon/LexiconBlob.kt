package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#blob
@Serializable
data class LexiconBlob(
    // TODO restrict to mime types? https://www.iana.org/assignments/media-types/media-types.xhtml
    val accept: List<String>? = null,
    override val description: String? = null,
    val maxSize: Int? = null,
): SchemaDef.Concrete, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.BLOB
}