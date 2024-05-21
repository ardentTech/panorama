package codegen

import lexicon.LexiconInteger

internal fun LexiconInteger.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<Int> {
    val cls = Int::class
    val validators = mutableListOf<String>()

    this.enum?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($name in listOf("${it.joinToString(separator = "\", \"")}"))
        """.trimIndent())
        }
    }
    this.maximum?.let {
        validators.add("""
require($name <= $it)
        """.trimIndent())
    }
    this.minimum?.let {
        validators.add("""
require($name >= $it)
        """.trimIndent())
    }

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
        validators = validators
    )
}