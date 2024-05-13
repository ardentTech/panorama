package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Cid(val s: String)