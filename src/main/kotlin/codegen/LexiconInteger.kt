package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconInteger

fun LexiconInteger.toPropertyConfig(keyName: String): PropertyConfig<Int> {
    val validators = mutableListOf<String>()
    this.enum?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($keyName in listOf("${it.joinToString(separator = "\", \"")}"))            
        """.trimIndent())
        }
    }
    this.maximum?.let {
        validators.add("""
require($keyName <= $it)
        """.trimIndent())
    }
    this.minimum?.let {
        validators.add("""
require($keyName >= $it)
        """.trimIndent())
    }

    return PropertyConfig(
        const = this.const,
        default = this.default,
        typeName = Int::class.asTypeName(),
        validators = validators,
    )
}