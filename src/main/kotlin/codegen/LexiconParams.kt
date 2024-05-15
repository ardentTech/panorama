package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

fun LexiconParams.Property.toPropertyConfig(keyName: String): PropertyConfig<*> {
    return when (this) {
        // TODO don't all of these need the keyName?
        is LexiconArray -> this.toPropertyConfig()
        is LexiconBoolean -> this.toPropertyConfig()
        is LexiconInteger -> this.toPropertyConfig(keyName)
        is LexiconString -> this.toPropertyConfig(keyName)
        is LexiconUnknown -> this.toPropertyConfig()
        else -> throw IllegalArgumentException("TODO LexiconParams.Property.toPropertyConfig(): $this")
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

        // const + default
        if (config.const == null) {
            val param = ParameterSpec.builder(key, config.typeName)
            config.default?.let {
                param.defaultValue(
                    if (config.typeName.toString() == "kotlin.String") "%S" else "%L", it)
            }
            constructorBuilder.addParameter(param.build())
        } // TODO else

        spec.addProperty(
            PropertySpec.builder(key, config.typeName).initializer(key).build()
        )

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