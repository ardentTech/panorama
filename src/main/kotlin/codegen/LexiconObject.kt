package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

// could be leaner but explicit it nice for now
fun LexiconObject.Property.toPropertyConfig(): PropertyConfig<*> {
    return when (this) {
        is LexiconBlob -> this.toPropertyConfig()
        is LexiconBoolean -> this.toPropertyConfig()
        is LexiconInteger -> this.toPropertyConfig()
        is LexiconString -> this.toPropertyConfig()
        // TODO array, bytes, cid-link, ref, unknown
        else -> throw IllegalArgumentException("TODO")
    }
}

// TODO what to do about `required`? express rules in unit tests...
// TODO if not required, set to null? could this conflict with `nullable`?
fun LexiconObject.codegen(name: String): TypeSpec {
    require(this.properties.isNotEmpty())

    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig()
        val typeName = if (this.nullable?.contains(key) == true) {
            config.typeName.copy(nullable = true)
        } else {
            config.typeName
        }

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
                constructorBuilder.addParameter(key, typeName)
            } else {
                constructorBuilder.addParameter(
                    ParameterSpec
                        .builder(key, typeName)
                        .defaultValue("%L", config.default)
                        .build()
                )
            }
        }
        spec.addProperty(
            PropertySpec.builder(key, typeName).initializer(key).build()
        )
    }

    spec.primaryConstructor(constructorBuilder.build())
    return spec.build()
}