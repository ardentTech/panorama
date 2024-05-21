package codegen

import lexicon.LexiconBoolean

internal fun LexiconBoolean.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<Boolean> {
    val cls = Boolean::class
    return this.const?.let {
        KBodyPropertyConfig(
            cls = cls,
            isNullable = isNullable,
            name = name,
            value = it
        )
    } ?: KConstructorPropertyConfig(
        cls = cls,
        defaultValue = this.default,
        isNullable = isNullable,
        name = name,
    )
}