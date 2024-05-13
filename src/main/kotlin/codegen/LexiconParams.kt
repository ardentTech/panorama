package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

fun LexiconParams.Property.toPropertyConfig(): PropertyConfig<*> {
    return when (this) {
        is LexiconBoolean -> this.toPropertyConfig()
        is LexiconInteger -> this.toPropertyConfig()
        is LexiconString -> this.toPropertyConfig()
        // TODO unknown
        else -> throw IllegalArgumentException("TODO")
    }
}

// TODO what to do about `required`? express rules in unit tests...
// TODO if not required, set to null?
fun LexiconParams.codegen(name: String): TypeSpec {
    require(this.properties.isNotEmpty())

    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig()

        // validators
        if (config.validators.isNotEmpty()) {
            val initBuilder = CodeBlock.builder()
            config.validators.forEach {
                initBuilder.addStatement(it.toString())
            }
            spec.addInitializerBlock(initBuilder.build())
        }

        // const + default
        if (config.const == null) {
            if (config.default == null) {
                constructorBuilder.addParameter(key, config.typeName)
            } else {
                constructorBuilder.addParameter(
                    ParameterSpec
                        .builder(key, config.typeName)
                        .defaultValue("%L", config.default)
                        .build()
                )
            }
        }
        spec.addProperty(
            PropertySpec.builder(key, config.typeName).initializer(key).build()
        )
    }

    spec.primaryConstructor(constructorBuilder.build())
    return spec.build()
}