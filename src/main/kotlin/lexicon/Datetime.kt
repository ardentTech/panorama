package lexicon

import kotlinx.serialization.Serializable

// https://atproto.com/specs/lexicon#datetime
@Serializable
@JvmInline
value class Datetime(val s: String)