package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#at-identifier
@Serializable
@JvmInline
value class AtIdentifier(val s: String)
// handle OR did
// did starts with: `did:`
// `:` is not allowed in handles