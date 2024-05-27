package codegen.converters

import codegen.SourceCode
import com.squareup.kotlinpoet.TypeSpec
import lexicon.*

object Primary {

    private fun List<PrimaryError>?.toEnum(name: String): TypeSpec? {
        return this?.let { errors ->
            SourceCode.generateEnum(errors.map { it.name }, null, name)
        }
    }

    // not worrying about PrimaryIO.encoding for now
    private fun PrimaryIO?.toDataClass(name: String): TypeSpec? {
        return this?.schema?.let {
            when (it) {
                is LexiconObject -> Container.toDataClass(it, name)
                else -> throw IllegalArgumentException("Unsupported type: $it")
            }
        }
    }

    fun toDataClasses(def: SchemaDef.Primary, name: String): List<TypeSpec> {
        return when (def) {
            is LexiconProcedure -> {
                val specs = mutableListOf<TypeSpec>()
                def.input.toDataClass("${name}Input")?.let { specs.add(it) }
                def.output.toDataClass("${name}Output")?.let { specs.add(it) }
                def.errors.toEnum("${name}Error")?.let { specs.add(it) }
                specs
            }
            else -> throw IllegalArgumentException("Unsupported type: $def")
        }
    }
}