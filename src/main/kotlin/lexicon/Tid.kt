package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Tid(val s: String)