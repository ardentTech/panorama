package codegen

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal data class KObjectConfig(
    val description: String? = null,
    val name: String,
    val properties: List<KBodyPropertyConfig<out Any>>
) {
    init {
        require(properties.isNotEmpty())
    }
}

internal fun generateKObject(config: KObjectConfig): TypeSpec {
    val spec = TypeSpec.objectBuilder(config.name)
        .addModifiers(KModifier.DATA)

    config.description?.let { spec.addKdoc(it) }

    config.properties.forEach { propertyConfig ->
        val propSpec = PropertySpec.builder(propertyConfig.name, propertyConfig.cls)

        propertyConfig.value?.let {
            propSpec.initializer(if (propertyConfig.cls == String::class) "%S" else "%L", it)
        }

        spec.addProperty(propSpec.build())
    }

    return spec.build()
}