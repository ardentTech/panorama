package codegen

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.Serializable
import lexicon.LexiconDoc
import java.nio.file.Path
import kotlin.reflect.KClass

// TODO use type-safe builder here?

internal data class KConstructorPropertyConfig(
    val cls: KClass<out Any>,
    val defaultValue: Any? = null,
    val isNullable: Boolean = false,
    val name: String,
    // TODO val validators:
)

internal data class KBodyPropertyConfig<T: Any>(
    val cls: KClass<T>,
    val isNullable: Boolean = false,
    val name: String,
    val value: T?,
)

// TODO constructorProperties cannot be empty
//internal data class KDataClassConfig(
//    val bodyProperties: List<KBodyPropertyConfig> = emptyList(),
//    val constructorProperties: List<KConstructorPropertyConfig>,
//    val description: String? = null,
//    val name: String
//)
//
//internal fun generateKDataClass(config: KDataClassConfig): TypeSpec {
//    val spec = TypeSpec.classBuilder(config.name)
//        .addAnnotation(Serializable::class)
//        .addModifiers(KModifier.DATA)
//
//    config.description?.let { spec.addKdoc(it) }
//
//    // TODO constructor properties
//    // TODO body properties
//
//    config.description?.let { spec.addKdoc(it) }
//
//    return spec.build()
//}

// TODO properties cannot be empty?
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

// nothing from KP propagates further than here
// this bridges ATP and KP
// TODO define formal interface
object LexiconCodeGenerator {

    fun generate(doc: LexiconDoc): Result<String> {
        // write to stdout
        return Result.success("package.File")
    }

    fun generate(doc: LexiconDoc, path: Path): Result<String> {
        // write to fs
        return Result.success("path.to.package.File")
    }

}
