package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconProcedure

fun LexiconProcedure.codegen(name: String): List<TypeSpec> {
    val specs = mutableListOf<TypeSpec>()

    // TODO description

    this.input?.let {
        specs += it.codegen("${name}Input")
    }

    this.output?.let {
        specs += it.codegen("${name}Output")
    }

    this.errors?.let {
        specs += it.codegen("${name}Errors")
    }

    return specs.toList()
}