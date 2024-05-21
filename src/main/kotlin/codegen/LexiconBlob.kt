package codegen

import lexicon.LexiconBlob

internal fun LexiconBlob.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<String> {

    val validators = mutableListOf<String>()
    this.accept?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($name in listOf("${it.joinToString(separator = "\", \"")}"))            
        """.trimIndent())
        }
    }
    this.maxSize?.let {
        validators.add("""
require($name.toByteArray().count() <= $it)            
        """.trimIndent())
    }

    return KConstructorPropertyConfig(
        cls = String::class,
        isNullable = isNullable,
        name = name,
        validators = validators
    )
}