package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconBoolean

fun LexiconBoolean.toPropertyConfig(keyName: String): PropertyConfig<Boolean> {
    return PropertyConfig(
        cls = Boolean::class,
        const = this.const,
        default = this.default,
        //typeName = Boolean::class.asTypeName()
    )
}