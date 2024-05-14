package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class RecordKey(val s: String)