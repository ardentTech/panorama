package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconInteger

fun LexiconInteger.toPropertyConfig(): PropertyConfig<Int> {
    val validators = mutableListOf<Validator<Int>>()
    this.enum?.let {
        validators.add { a -> a in it }
    }
    this.maximum?.let {
        validators.add { a -> a <= it }
    }
    this.minimum?.let {
        validators.add { a -> a >= it }
    }

    return PropertyConfig(
        const = this.const,
        default = this.default,
        typeName = Int::class.asTypeName(),
        validators = validators,
    )
}