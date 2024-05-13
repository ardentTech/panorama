package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class AtIdentifier(val s: String)