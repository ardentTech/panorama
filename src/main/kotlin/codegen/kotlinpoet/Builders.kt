package codegen.kotlinpoet

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

// these functions aim to provide a more friendly API for building KotlinPoet specs

internal fun buildEnum(constants: List<String>, description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.enumBuilder(name)
    description?.let { spec.addKdoc(it) }
    constants.forEach { spec.addEnumConstant(it) }
    return spec.build()
}

internal fun buildDataObject(description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.objectBuilder(name)
        .addModifiers(KModifier.DATA)
    description?.let { spec.addKdoc(it) }
    return spec.build()
}