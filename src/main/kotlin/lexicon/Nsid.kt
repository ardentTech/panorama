package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/did
// https://atproto.com/specs/nsid
@Serializable
@JvmInline
value class Nsid(val s: String)