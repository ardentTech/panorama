package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

// could be leaner but explicit it nice for now
fun LexiconObject.Property.toPropertyConfig(keyName: String): PropertyConfig<*> {
    return when (this) {
        // TODO don't all of these need the keyName?
        is LexiconArray -> this.toPropertyConfig()
        is LexiconBlob -> this.toPropertyConfig(keyName)
        is LexiconBoolean -> this.toPropertyConfig()
        is LexiconBytes -> this.toPropertyConfig(keyName)
        is LexiconCidLink -> this.toPropertyConfig(keyName)
        is LexiconInteger -> this.toPropertyConfig(keyName)
        is LexiconRef -> this.toPropertyConfig()
        is LexiconString -> this.toPropertyConfig(keyName)
        is LexiconUnion -> this.toPropertyConfig()
        is LexiconUnknown -> this.toPropertyConfig()
    }
}

// TODO what to do about `required`? express rules in unit tests...
// TODO if not required, set to null? could this conflict with `nullable`?
fun LexiconObject.codegen(name: String): TypeSpec {
    require(this.properties.isNotEmpty())

    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()
    val validators = mutableListOf<String>()

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig(key)

        val typeName = if (this.nullable?.contains(key) == true) {
            config.typeName.copy(nullable = true)
        } else {
            config.typeName
        }

        // const + default
        if (config.const == null) {
            val param = ParameterSpec.builder(key, typeName)
            config.default?.let {
                param.defaultValue(
                    if (typeName.toString() == "kotlin.String") "%S" else "%L", it)
            }
            constructorBuilder.addParameter(param.build())
        } // TODO else

        spec.addProperty(
            PropertySpec.builder(key, typeName).initializer(key).build()
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