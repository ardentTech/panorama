package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconBlob

fun LexiconBlob.toPropertyConfig(): PropertyConfig<String> {
    val validators = mutableListOf<Validator<String>>()
    this.accept?.let {
        validators.add { a -> a in it }
    }
    // TODO this should be max size in bytes
    this.maxSize?.let {
        validators.add { a -> a.count() <= it }
    }

    return PropertyConfig(
        const = null,
        default = null,
        typeName = String::class.asTypeName(),
        validators = validators
    )
}