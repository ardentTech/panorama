package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/handle
@Serializable
@JvmInline
value class Handle(val s: String)