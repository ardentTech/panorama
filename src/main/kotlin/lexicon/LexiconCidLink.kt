package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#cid-link
@Serializable
data class LexiconCidLink(
    override val description: String? = null
): SchemaDef.Concrete, LexiconArray.Items, LexiconObject.Property {
    override val type = LexiconType.CID_LINK
}