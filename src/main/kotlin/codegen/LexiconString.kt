package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.*

// TODO: maxGraphemes, minGraphemes
fun LexiconString.toPropertyConfig(keyName: String): PropertyConfig<String> {
    val validators = mutableListOf<String>()
    this.enum?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($keyName in listOf("${it.joinToString(separator = "\", \"")}"))            
        """.trimIndent())
        }
    }
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

    val typeName = this.format?.let {
        when (it) {
            LexiconString.Format.AT_IDENTIFIER -> AtIdentifier::class
            LexiconString.Format.AT_URI -> AtUri::class
            LexiconString.Format.CID -> Cid::class
            LexiconString.Format.DATETIME -> Datetime::class
            LexiconString.Format.DID -> Did::class
            LexiconString.Format.HANDLE -> Handle::class
            LexiconString.Format.LANGUAGE -> Language::class
            LexiconString.Format.NSID -> Nsid::class
            LexiconString.Format.RECORD_KEY -> RecordKey::class
            LexiconString.Format.TID -> Tid::class
            LexiconString.Format.URI -> Uri::class
        }.asTypeName()
    } ?: String::class.asTypeName()

    return PropertyConfig(
        const = this.const,
        default = this.default,
        typeName = typeName
    )
}