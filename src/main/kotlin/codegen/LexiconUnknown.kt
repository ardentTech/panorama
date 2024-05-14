package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconUnknown

// TODO decide what to do here
fun LexiconUnknown.toPropertyConfig(): PropertyConfig<String> {
    return PropertyConfig(
        const = null,
        default = null,
        typeName = Unit::class.asTypeName()
    )
}