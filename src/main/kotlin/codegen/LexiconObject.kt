package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

internal fun LexiconObject.Property.toConfig(name: String, isNullable: Boolean): KPropertyConfig<*> {
    return when (this) {
        is LexiconArray -> this.toPropertyConfig(name, isNullable)
        is LexiconBlob -> this.toPropertyConfig(name, isNullable)
        is LexiconBoolean -> this.toPropertyConfig(name, isNullable)
        is LexiconBytes -> this.toPropertyConfig(name, isNullable)
        is LexiconCidLink -> this.toPropertyConfig(name, isNullable)
        is LexiconInteger -> this.toPropertyConfig(name, isNullable)
        is LexiconRef -> this.toPropertyConfig(name, isNullable)
        is LexiconString -> this.toPropertyConfig(name, isNullable)
        is LexiconUnion -> this.toPropertyConfig(name, isNullable)
        is LexiconUnknown -> this.toPropertyConfig(name, isNullable)
    }
}

internal fun LexiconObject.codegen(name: String): TypeSpec {
    val bodyProperties = mutableListOf<KBodyPropertyConfig<out Any>>()
    val constructorProperties = mutableListOf<KConstructorPropertyConfig<out Any>>()

    this.properties.forEach { (key, value) ->
        when(val config = value.toConfig(key, this.nullable?.contains(key) == true )) {
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