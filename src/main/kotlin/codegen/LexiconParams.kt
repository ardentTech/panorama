package codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import lexicon.*

fun LexiconParams.Property.toPropertyConfig(keyName: String): PropertyConfig<*> {
    return when (this) {
        is LexiconArray -> this.toPropertyConfig(keyName)
        is LexiconBoolean -> this.toPropertyConfig(keyName)
        is LexiconInteger -> this.toPropertyConfig(keyName)
        is LexiconString -> this.toPropertyConfig(keyName)
        is LexiconUnknown -> this.toPropertyConfig(keyName)
    }
}

// TODO what to do about `required`? express rules in unit tests...
// TODO if not required, set to null?
fun LexiconParams.codegen(name: String): TypeSpec {
    require(this.properties.isNotEmpty())

    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()
    val validators = mutableListOf<String>()

    this.description?.let { spec.addKdoc(it) }

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig(key)

        val typeName = if (config.cls == List::class) {
            // TODO not a fan
            val parts = config.itemCls!!.qualifiedName!!.split(".")
            val packageName = parts.slice(0..(parts.count() - 2)).joinToString(".")

            config.cls.asTypeName().parameterizedBy(ClassName(packageName, parts.last()))
        } else {
            config.cls.asTypeName()
        }

        config.const?.let { const ->
            spec.addProperty(
                PropertySpec.builder(key, typeName)
                    .initializer("%L", const)
                    .build()
            )
        } ?: run {
            // default
            val param = ParameterSpec.builder(key, typeName)
            config.default?.let {
                param.defaultValue(
                    if (config.cls == String::class) "%S" else "%L", it)
            }
            constructorBuilder.addParameter(param.build())
            spec.addProperty(
                PropertySpec.builder(key, typeName).initializer(key).build()
            )
        }

        validators += config.validators
    }

    spec.primaryConstructor(constructorBuilder.build())

    // must finish building constructor before introducing init block
    if (validators.isNotEmpty()) {
        val initBuilder = CodeBlock.builder()
        validators.forEach {
            initBuilder.addStatement(it)
        }
        spec.addInitializerBlock(initBuilder.build())
    }

    return spec.build()
}