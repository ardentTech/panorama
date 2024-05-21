package codegen

import com.squareup.kotlinpoet.TypeSpec

// TODO test
internal fun generateKEnum(name: String, constants: List<String>): TypeSpec {
    val spec = TypeSpec.enumBuilder(name)
    constants.forEach { spec.addEnumConstant(it) }
    return spec.build()
}