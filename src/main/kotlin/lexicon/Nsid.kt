package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/did
@Serializable
@JvmInline
value class Nsid(val s: String)