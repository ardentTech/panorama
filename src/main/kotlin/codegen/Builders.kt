package codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// these functions aim to provide a more friendly API for building necessary KotlinPoet specs

fun ParameterSpec.toProperty(): PropertySpec = PropertySpec.builder(name, type).initializer(name).build()

internal fun buildDataClass(
    description: String? = null,
    name: String,
    parameters: List<ParameterSpec>,
    properties: List<PropertySpec> = emptyList()
): TypeSpec {
    require(parameters.isNotEmpty())
    val spec = TypeSpec.classBuilder(name)
        .addAnnotation(Serializable::class)
        .addModifiers(KModifier.DATA)
    description?.let { spec.addKdoc(it) }

    val constructor = FunSpec.constructorBuilder()
    constructor.addParameters(parameters)
    spec.addProperties(parameters.map { it.toProperty() })
    spec.primaryConstructor(constructor.build())

    spec.addProperties(properties)

    // TODO validators

    return spec.build()
}

internal fun buildDataObject(description: String? = null, name: String, properties: List<PropertySpec> = emptyList()): TypeSpec {
    val spec = TypeSpec.objectBuilder(name)
        .addAnnotation(Serializable::class)
        .addModifiers(KModifier.DATA)
    description?.let { spec.addKdoc(it) }
    spec.addProperties(properties) // TODO unit test
    return spec.build()
}

internal fun buildEnum(constants: List<String>, description: String? = null, name: String): TypeSpec {
    val spec = TypeSpec.enumBuilder(name)
    description?.let { spec.addKdoc(it) }
    constants.forEach { spec.addEnumConstant(it) }
    return spec.build()
}

// TODO unit test
internal fun buildFile(comment: String, contents: List<TypeSpec>, packageName: String, fileName: String): FileSpec {
    return FileSpec.builder(packageName, fileName)
        .addFileComment(comment)
        .addTypes(contents)
        .build()
}

// TODO does this need to support a default value?
internal fun buildListParameter(isNullable: Boolean = false, itemCls: KClass<*>, name: String): ParameterSpec =
    ParameterSpec.builder(name, List::class.asClassName().parameterizedBy(itemCls.asTypeName()).copy(nullable = isNullable)).build()

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

internal fun <T: Any> buildValueClass(cls: KClass<T>, name: String): TypeSpec {
    return TypeSpec.classBuilder(name)
        .addAnnotation(Serializable::class)
        .addModifiers(KModifier.VALUE)
        .primaryConstructor(
            FunSpec.constructorBuilder().addParameter("value", cls).build()
        )
        .addProperty(
            PropertySpec.builder("value", cls).initializer("value").build()
        )
        .build()
}