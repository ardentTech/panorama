package codegen

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.SchemaDef


interface ParameterConverter <T: SchemaDef> {
    fun toParameter(def: T, isNullable: Boolean = false, name: String): ParameterSpec
}

interface PropertyConverter <T: SchemaDef> {
    fun toProperty(def: T, name: String): PropertySpec
}

interface TypeConverter <T: SchemaDef> {
    fun toType(def: T, name: String): TypeSpec
}

interface TypesConverter <T: SchemaDef> {
    fun toTypes(def: T, name: String): List<TypeSpec>
}