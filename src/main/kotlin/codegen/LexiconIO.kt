package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconIO
import lexicon.LexiconObject
import lexicon.LexiconRef

fun LexiconIO.codegen(name: String): TypeSpec {
    return this.schema?.let { schema ->
        when (schema) {
            is LexiconObject -> schema.codegen(name)
            is LexiconRef -> schema.codegen(name)
            // TODO union
            else -> throw IllegalArgumentException("TODO LexiconIO.codegen() name: $name / schema: ${this.schema}")
        }
    } ?: run {
        generateKObject(
            KObjectConfig(
                name = name, properties = listOf(
                    KBodyPropertyConfig(cls = String::class, name = "encoding", value = this.encoding,)
                )
            )
        )
    }
}