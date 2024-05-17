package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconBytes

fun LexiconBytes.toPropertyConfig(keyName: String): PropertyConfig<String> {
    val validators = mutableListOf<String>()
    this.maxLength?.let {
        validators.add("""
require(${keyName.count()} <= $it)
        """.trimIndent())
    }
    this.minLength?.let {
        validators.add("""
require(${keyName.count()} >= $it)
        """.trimIndent())
    }

    return PropertyConfig(
        cls = String::class,
        const = null,
        default = null,
        //typeName = String::class.asTypeName(),
        validators = validators
    )
}