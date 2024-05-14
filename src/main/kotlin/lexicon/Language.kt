package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#language
@Serializable
@JvmInline
value class Language(val s: String)