package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconUnknown

// TODO does this need a more specific type than Any?
// Indicates than any data could appear at this location, with no specific validation.
fun LexiconUnknown.toPropertyConfig(keyName: String): PropertyConfig<Any> {
    return PropertyConfig(
        cls = Any::class,
        const = null,
        default = null,
        // TODO decide what to do here
        //typeName = Unit::class.asTypeName()
    )
}