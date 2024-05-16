package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconUnknown

fun LexiconUnknown.toPropertyConfig(keyName: String): PropertyConfig<String> {
    return PropertyConfig(
        const = null,
        default = null,
        // TODO decide what to do here
        typeName = Unit::class.asTypeName()
    )
}