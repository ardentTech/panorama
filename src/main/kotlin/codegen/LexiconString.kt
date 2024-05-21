package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.*

// TODO: maxGraphemes, minGraphemes
// TODO: format... could inline format-specific validators instead of opting for value classes
internal fun LexiconString.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<String> {
//    val cls = this.format?.let {
//        when (it) {
//            LexiconString.Format.AT_IDENTIFIER -> AtIdentifier::class
//            LexiconString.Format.AT_URI -> AtUri::class
//            LexiconString.Format.CID -> Cid::class
//            LexiconString.Format.DATETIME -> Datetime::class
//            LexiconString.Format.DID -> Did::class
//            LexiconString.Format.HANDLE -> Handle::class
//            LexiconString.Format.LANGUAGE -> Language::class
//            LexiconString.Format.NSID -> Nsid::class
//            LexiconString.Format.RECORD_KEY -> RecordKey::class
//            LexiconString.Format.TID -> Tid::class
//            LexiconString.Format.URI -> Uri::class
//        }
//    } ?: String::class
    val cls = String::class
    val validators = mutableListOf<String>()

    this.enum?.let {
        if (it.isNotEmpty()) {
            validators.add("""
require($name in listOf("${it.joinToString(separator = "\", \"")}"))            
        """.trimIndent())
        }
    }
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