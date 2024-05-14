package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

// could be leaner but explicit it nice for now
fun LexiconObject.Property.toPropertyConfig(keyName: String): PropertyConfig<*> {
    return when (this) {
        is LexiconArray -> this.toPropertyConfig()
        is LexiconBlob -> this.toPropertyConfig(keyName)
        is LexiconBoolean -> this.toPropertyConfig()
        is LexiconInteger -> this.toPropertyConfig(keyName)
        is LexiconRef -> this.toPropertyConfig()
        is LexiconString -> this.toPropertyConfig(keyName)
        is LexiconUnknown -> this.toPropertyConfig()
        // TODO bytes, cid-link
        else -> throw IllegalArgumentException("TODO LexiconObject.Property.toPropertyConfig(): $this")
    }
}

// TODO what to do about `required`? express rules in unit tests...
// TODO if not required, set to null? could this conflict with `nullable`?
fun LexiconObject.codegen(name: String): TypeSpec {
    println("LexiconObject.codegen: $name")
    require(this.properties.isNotEmpty())

    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()

    this.properties.forEach { (key, value) ->
        val config = value.toPropertyConfig(key)

        val typeName = if (this.nullable?.contains(key) == true) {
            config.typeName.copy(nullable = true)
        } else {
            config.typeName
        }

        // const + default
        if (config.const == null) {
            println("const is null for $key")
            if (config.default == null) {
                println("default is null for $key")
                constructorBuilder.addParameter(key, typeName)
            } else {
                println("default is not null for $key")
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

        // validators - this must come after initializing data class values to avoid weirdness
        if (config.validators.isNotEmpty()) {
            val initBuilder = CodeBlock.builder()
            config.validators.forEach {
                initBuilder.addStatement(it)
            }
            spec.addInitializerBlock(initBuilder.build())
        }
    }

    spec.primaryConstructor(constructorBuilder.build())
    return spec.build()
}