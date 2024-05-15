package codegen

import com.squareup.kotlinpoet.PropertySpec
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
        TypeSpec.objectBuilder(name)
            .addProperty(
                PropertySpec.builder("encoding", String::class)
                    .initializer("%S", this.encoding)
                    .build()
            )
        .build()
    }
}