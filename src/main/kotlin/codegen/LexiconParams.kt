package codegen

import com.squareup.kotlinpoet.*
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

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig(key)
        val typeName = config.cls.asTypeName()

        println("string: ${config.cls}")

        // const + default
        config.const?.let { const ->
            spec.addProperty(
                PropertySpec.builder(key, typeName)
                    .initializer("%L", const)
                    .build()
            )
        } ?: run {
            val param = ParameterSpec.builder(key, typeName)
            config.default?.let {
                param.defaultValue(
                    if (typeName.toString() == "kotlin.String") "%S" else "%L", it)
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