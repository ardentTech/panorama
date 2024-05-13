package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/did
@Serializable
@JvmInline
value class Did(val s: String)
// TODO lots of validation