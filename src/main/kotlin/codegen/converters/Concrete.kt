package codegen.converters

import codegen.SourceCode
import com.squareup.kotlinpoet.ParameterSpec
import lexicon.LexiconString
import lexicon.SchemaDef

object Concrete {
    fun toParameter(def: SchemaDef.Concrete, isNullable: Boolean, name: String): ParameterSpec {
        return when (def) {
            is LexiconString -> SourceCode.generateParameter(String::class, def.default, isNullable, name)
            else -> throw IllegalArgumentException("Unsupported type: $def")
        }
    }
}