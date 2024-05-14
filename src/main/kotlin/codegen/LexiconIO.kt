package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconIO
import lexicon.LexiconObject
import lexicon.LexiconRef

fun LexiconIO.codegen(name: String): TypeSpec = when (this.schema) {
    is LexiconObject -> this.schema.codegen(name)
    is LexiconRef -> this.schema.codegen(name)
    // TODO union
    else -> throw IllegalArgumentException("TODO")
}