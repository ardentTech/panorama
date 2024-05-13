package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconIO
import lexicon.LexiconObject

fun LexiconIO.codegen(name: String): TypeSpec = when (this.schema) {
    is LexiconObject -> this.schema.codegen(name)
    // TODO ref, union
    else -> throw IllegalArgumentException("TODO")
}