package codegen.kotlinpoet

import com.squareup.kotlinpoet.*
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

internal fun <T: Any> buildParameter(cls: KClass<T>, default: T? = null, isNullable: Boolean = false, name: String): ParameterSpec {
    val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
    default?.let { spec.defaultValue(formatterFor(cls), it) }
    return spec.build()
}

internal fun <T: Any> buildProperty(cls: KClass<T>, name: String, value: T? = null): PropertySpec {
    val spec = PropertySpec.builder(name, cls)
    value?.let { spec.initializer(formatterFor(cls), it) }
    return spec.build()
}

internal fun <T: Any> formatterFor(cls: KClass<T>): String = if (cls == String::class) "%S" else "%L"