package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Uri(val s: String)