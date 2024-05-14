package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/record-key
@Serializable
@JvmInline
value class RecordKey(val s: String)