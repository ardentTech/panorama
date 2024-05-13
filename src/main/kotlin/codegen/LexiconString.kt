package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.*

// TODO: format, maxGraphemes, minGraphemes, knownValues
fun LexiconString.toPropertyConfig(): PropertyConfig<String> {
    println("LexiconString.toPropertyConfig()")
    val validators = mutableListOf<Validator<String>>()
    this.enum?.let {
        validators.add { a -> a in it }
    }
    this.maxLength?.let {
        validators.add { a -> a.count() <= it }
    }
    this.minLength?.let {
        validators.add { a -> a.count() >= it }
    }

    val typeName = this.format?.let {
        when (it) {
            LexiconString.Format.AT_IDENTIFIER -> AtIdentifier::class
            LexiconString.Format.CID -> Cid::class
            LexiconString.Format.DID -> Did::class
            LexiconString.Format.NSID -> Nsid::class
            // TODO expand
            else -> throw IllegalArgumentException("TODO")
        }.asTypeName()
    } ?: String::class.asTypeName()

    return PropertyConfig(
        const = this.const,
        default = this.default,
        typeName = typeName
    )
}