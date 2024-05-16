package codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.*
import java.nio.file.Path

fun SchemaDef.codegen(defName: String): List<TypeSpec> {
    return when (this) {
        is LexiconObject -> listOf(this.codegen(defName)) // TODO need a `defs` type to wrap multiple objects and avoid listOf?
        is LexiconProcedure -> this.codegen(defName)
        is LexiconQuery -> this.codegen(defName)
        is LexiconRecord -> listOf(this.codegen(defName))
        is LexiconSubscription -> this.codegen(defName)
        is LexiconToken -> listOf(this.codegen(defName))
        else -> emptyList()
    }
}

fun LexiconDoc.codegen(destination: Path) {
    var description: String? = null
    val lexiconName = this.id.split(".").last().capitalize()
    val specs = mutableListOf<TypeSpec>()

    this.defs.forEach { (defName, schemaDef) ->
        specs += schemaDef.codegen(
            if (defName == "main") lexiconName
            else if (lexiconName == "Defs") defName.capitalize()
            else "${lexiconName}${defName.capitalize()}"
        )
        if (defName == "main") description = schemaDef.description
    }

    if (specs.isNotEmpty()) {
        val file = FileSpec.builder(this.namespace ?: "", lexiconName)
            .addFileComment("""
This file was generated by Panorama. DO NOT EDIT.

Lexicon: ${this.id}
${description?.let { "Description: $it" }}
                        """.trimIndent())
        specs.forEach { file.addType(it) }
        file.build().writeTo(destination)
    }
    // TODO return relative path to generated file?
}