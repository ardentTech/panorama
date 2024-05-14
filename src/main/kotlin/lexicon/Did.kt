package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/did
// https://atproto.com/specs/did#at-protocol-dids
@Serializable
@JvmInline
value class Did(val s: String)
// TODO lots of validation