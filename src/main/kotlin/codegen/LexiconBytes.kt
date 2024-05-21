package codegen

import lexicon.LexiconBytes

internal fun LexiconBytes.toPropertyConfig(name: String, isNullable: Boolean = false): KConstructorPropertyConfig<String> {
    val validators = mutableListOf<String>()
    this.maxLength?.let {
        validators.add("""
require(${name.count()} <= $it)
        """.trimIndent())
    }
    this.minLength?.let {
        validators.add("""
require(${name.count()} >= $it)
        """.trimIndent())
    }

    return KConstructorPropertyConfig(
        cls = String::class,
        isNullable = isNullable,
        name = name,
        validators = validators
    )
}