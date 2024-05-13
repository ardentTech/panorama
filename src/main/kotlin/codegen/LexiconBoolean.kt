package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconBoolean

fun LexiconBoolean.toPropertyConfig(): PropertyConfig<Boolean> {
    return PropertyConfig(
        const = this.const,
        default = this.default,
        typeName = Boolean::class.asTypeName()
    )
}