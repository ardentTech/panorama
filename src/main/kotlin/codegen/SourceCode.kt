package codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

internal object SourceCode {

    private fun ParameterSpec.toProperty(): PropertySpec = PropertySpec.builder(name, type).initializer(name).build()

    fun <T: Any> formatterFor(cls: KClass<T>): String = if (cls == String::class) "%S" else "%L"

    fun generateDataClass(
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

    fun generateDataObject(description: String? = null, name: String, properties: List<PropertySpec> = emptyList()): TypeSpec {
        val spec = TypeSpec.objectBuilder(name)
            .addAnnotation(Serializable::class)
            .addModifiers(KModifier.DATA)
        description?.let { spec.addKdoc(it) }
        spec.addProperties(properties) // TODO unit test
        return spec.build()
    }

    fun generateEnum(constants: List<String>, description: String? = null, name: String): TypeSpec {
        val spec = TypeSpec.enumBuilder(name)
        description?.let { spec.addKdoc(it) }
        constants.forEach { spec.addEnumConstant(it) }
        return spec.build()
    }

    fun generateFile(comment: String, contents: List<TypeSpec>, packageName: String, fileName: String): FileSpec {
        require(contents.isNotEmpty())
        return FileSpec.builder(packageName, fileName)
            .addFileComment(comment)
            .addTypes(contents)
            .build()
    }

    // TODO does this need to support a default value?
    fun generateListParameter(isNullable: Boolean = false, itemCls: KClass<*>, name: String): ParameterSpec =
        ParameterSpec.builder(name, List::class.asClassName().parameterizedBy(itemCls.asTypeName()).copy(nullable = isNullable)).build()

    fun <T: Any> generateParameter(cls: KClass<T>, default: T? = null, isNullable: Boolean = false, name: String): ParameterSpec {
        val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
        default?.let { spec.defaultValue(formatterFor(cls), it) }
        return spec.build()
    }

    fun <T: Any> generateProperty(cls: KClass<T>, name: String, value: T? = null): PropertySpec {
        val spec = PropertySpec.builder(name, cls)
        value?.let { spec.initializer(formatterFor(cls), it) }
        return spec.build()
    }
}