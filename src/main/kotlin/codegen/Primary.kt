package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconObject
import lexicon.LexiconProcedure
import lexicon.LexiconRef
import lexicon.LexiconUnion

// query, record, subscription

interface LexiconIOConverter {
    fun toType(def: LexiconObject, name: String) = LexiconObjectConverter.toType(def, "${name}Input")
    fun toType(def: LexiconRef, name: String): TypeSpec
    fun toType(def: LexiconUnion, name: String): TypeSpec
}

object LexiconProcedureConverter: TypesConverter<LexiconProcedure> {

    override fun toTypes(def: LexiconProcedure, name: String): List<TypeSpec> {
        def.input?.let { input ->
            val inputName = "${name}Input"
            when (input.schema) {
                is LexiconObject -> LexiconObjectConverter.toType(input.schema, inputName)
                is LexiconRef -> LexiconRefConverter.toType(input.schema, inputName)
                is LexiconUnion -> LexiconUnionConverter.toType(input.schema, inputName)
                else -> throw IllegalArgumentException("Unsupported input type: ${input.schema}")
            }
        }

        def.output?.let { output ->
            val outputName = "${name}Output"
            when (output.schema) {
                is LexiconObject -> LexiconObjectConverter.toType(output.schema, outputName)
                is LexiconRef -> LexiconRefConverter.toType(output.schema, outputName)
                is LexiconUnion -> LexiconUnionConverter.toType(output.schema, outputName)
                else -> throw IllegalArgumentException("Unsupported input type: ${output.schema}")
            }
        }

        def.errors?.let { errors ->
            buildEnum(errors.map { it.name.camelToEnumCase() }, null, "${name}Error")
        }

        return emptyList()
    }
}