package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconSubscription

fun LexiconSubscription.codegen(name: String): List<TypeSpec> {
    val specs = mutableListOf<TypeSpec>()

    // TODO description

    this.parameters?.let {
        specs += it.codegen("${name}Params")
    }

    // TODO message (schema must be a union of refs)
    // this is the output from the web socket

    this.errors?.let {
        specs += it.codegen("${name}Errors")
    }

    return specs.toList()
}