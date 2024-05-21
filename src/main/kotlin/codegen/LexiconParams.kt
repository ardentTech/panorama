package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.*

internal fun LexiconParams.Property.toConfig(name: String): KPropertyConfig<*> {
    return when (this) {
        is LexiconArray -> this.toPropertyConfig(name)
        is LexiconBoolean -> this.toPropertyConfig(name)
        is LexiconInteger -> this.toPropertyConfig(name)
        is LexiconString -> this.toPropertyConfig(name)
        is LexiconUnknown -> this.toPropertyConfig(name)
    }
}

internal fun LexiconParams.codegen(name: String): TypeSpec {
    val bodyProperties = mutableListOf<KBodyPropertyConfig<out Any>>()
    val constructorProperties = mutableListOf<KConstructorPropertyConfig<out Any>>()

    this.properties.forEach { (key, value) ->
        when(val config = value.toConfig(key)) {
            is KBodyPropertyConfig<*> -> bodyProperties.add(config)
            is KConstructorPropertyConfig<*> -> constructorProperties.add(config)
        }
    }

    return generateKDataClass(
        KDataClassConfig(
            bodyProperties = bodyProperties,
            constructorProperties = constructorProperties,
            description = this.description,
            name = name
        )
    )
}