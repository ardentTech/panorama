package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Datetime(val s: String)