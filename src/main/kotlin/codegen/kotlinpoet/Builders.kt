package codegen.kotlinpoet

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlin.reflect.KClass

// these functions aim to provide a more friendly API for building KotlinPoet specs

internal fun buildDataObject(description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.objectBuilder(name)
        .addModifiers(KModifier.DATA)
    description?.let { spec.addKdoc(it) }
    return spec.build()
}

internal fun buildEnum(constants: List<String>, description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.enumBuilder(name)
    description?.let { spec.addKdoc(it) }
    constants.forEach { spec.addEnumConstant(it) }
    return spec.build()
}

internal fun <T: Any> buildProperty(cls: KClass<T>, name: String, value: T? = null): PropertySpec {
    val spec = PropertySpec.builder(name, cls)
    value?.let { spec.initializer(if (cls == String::class) "%S" else "%L", it) }
    return spec.build()
}