package codegen.kotlinpoet

import com.squareup.kotlinpoet.TypeSpec

internal fun buildEnum(constants: List<String>, description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.enumBuilder(name)
    description?.let { spec.addKdoc(it) }
    constants.forEach { spec.addEnumConstant(it) }
    return spec.build()
}