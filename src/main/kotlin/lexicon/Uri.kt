package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#uri
// https://atproto.com/specs/at-uri-scheme
@Serializable
@JvmInline
value class Uri(val s: String)