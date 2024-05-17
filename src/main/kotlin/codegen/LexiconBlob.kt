package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconBlob

fun LexiconBlob.toPropertyConfig(keyName: String): PropertyConfig<String> {

    val validators = mutableListOf<String>()
    this.accept?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($keyName in listOf("${it.joinToString(separator = "\", \"")}"))            
        """.trimIndent())
        }
    }
    this.maxSize?.let {
        validators.add("""
require($keyName.toByteArray().count() <= $it)            
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