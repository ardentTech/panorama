package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Language(val s: String)