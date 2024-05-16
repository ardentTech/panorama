package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconQuery

fun LexiconQuery.codegen(name: String): List<TypeSpec> {
    val specs = mutableListOf<TypeSpec>()

    // TODO description

    this.parameters?.let {
        specs += it.codegen("${name}Params")
    }

    this.output?.let {
        specs += it.codegen("${name}Output")
    }

    this.errors?.let {
        specs += it.codegen(name)
    }

    return specs.toList()
}