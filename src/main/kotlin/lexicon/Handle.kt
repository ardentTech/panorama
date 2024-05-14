package lexicon

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Handle(val s: String)