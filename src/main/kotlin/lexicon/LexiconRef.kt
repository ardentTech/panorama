package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#ref
@Serializable
data class LexiconRef(
    override val description: String? = null,
    val ref: String
):
    SchemaDef.Meta,
    LexiconArray.Items,
    LexiconObject.Property,
    LexiconProcedure.IO.Schema,
    LexiconQuery.IO.Schema
{
    override val type = LexiconType.REF
}