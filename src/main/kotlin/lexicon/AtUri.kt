package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AtUri(val s: String)