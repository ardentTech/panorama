package codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

// TODO constructorProperties cannot be empty
internal data class KDataClassConfig(
    val bodyProperties: List<KBodyPropertyConfig<out Any>> = emptyList(),
    val constructorProperties: List<KConstructorPropertyConfig<out Any>>,
    val description: String? = null,
    val name: String
)

// TODO skip LexiconUnknown for now
internal fun generateKDataClass(config: KDataClassConfig): TypeSpec {
    val spec = TypeSpec.classBuilder(config.name)
        .addAnnotation(Serializable::class)
        .addModifiers(KModifier.DATA)
    val constructorBuilder = FunSpec.constructorBuilder()
    val validators = mutableListOf<String>()

    config.description?.let { spec.addKdoc(it) }

    config.constructorProperties.forEach {
        val typeName = typeNameFor(it.cls, it.itemCls, it.isNullable)

        val param = ParameterSpec.builder(it.name, typeName)
        it.defaultValue?.let { default ->
            param.defaultValue(formatterFor(it.cls), default)
        }
        constructorBuilder.addParameter(param.build())
        spec.addProperty(
            PropertySpec.builder(it.name, typeName).initializer(it.name).build()
        )
        validators += it.validators
    }

    config.bodyProperties.forEach {
        val typeName = typeNameFor(it.cls, it.itemCls, it.isNullable)
        spec.addProperty(
            PropertySpec.builder(it.name, typeName)
                .initializer(formatterFor(it.cls), it.value)
                .build()
        )
    }

    spec.primaryConstructor(constructorBuilder.build())

    // must finish building constructor before introducing this init block
    if (validators.isNotEmpty()) {
        val initBuilder = CodeBlock.builder()
        validators.forEach { initBuilder.addStatement(it) }
        spec.addInitializerBlock(initBuilder.build())
    }

    return spec.build()
}

internal fun formatterFor(cls: KClass<out Any>) = if (cls == String::class) "%S" else "%L"

// TODO achieve nullability without needing `.copy()`
internal fun typeNameFor(cls: KClass<out Any>, itemCls: KClass<out Any>?, isNullable: Boolean) =
    if (cls == List::class) {
        val parts = itemCls!!.qualifiedName!!.split(".")
        // this seems fragile...
        val packageName = parts.slice(0..(parts.count() - 2)).joinToString(".")
        cls.asTypeName().parameterizedBy(
            ClassName(packageName, parts.last()).copy(nullable = isNullable)
        )
    } else {
        cls.asTypeName().copy(nullable = isNullable)
    }