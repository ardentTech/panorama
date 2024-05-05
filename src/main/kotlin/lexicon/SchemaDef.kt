package lexicon

import kotlinx.serialization.Serializable

// "The schema definition language for atproto is Lexicon."
@Serializable
sealed interface SchemaDef {
    val description: String?
    val type: String

    sealed interface PrimaryType: SchemaDef
    sealed interface FieldType: SchemaDef
}