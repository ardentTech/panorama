package codegen.converters

import codegen.SourceCode
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconObject
import lexicon.SchemaDef

object Container {
    fun toDataClass(def: SchemaDef.Container, name: String): TypeSpec {
        return when (def) {
            is LexiconObject -> {
                val parameters = mutableListOf<ParameterSpec>()
                def.properties.forEach { (key, value) ->
                    parameters.add(
                        when (value) {
                            is SchemaDef.Concrete -> Concrete.toParameter(value, def.nullable?.contains(key) == true, key)
                            else -> throw IllegalArgumentException("Unsupported type: $value")
                        }
                    )
                }
                SourceCode.generateDataClass(def.description, name, parameters)
            }
            else -> throw IllegalArgumentException("Unsupported type: $def")
        }
    }
}