package lexicon

import kotlinx.serialization.Serializable

// "The schema definition language for atproto is Lexicon."
@Serializable
sealed interface SchemaDef {
    val description: String?
    val type: String

    sealed interface Concrete: SchemaDef
    sealed interface Container: SchemaDef
    sealed interface Meta: SchemaDef
    sealed interface Primary: SchemaDef
}