package codegen.transformers

import codegen.KtAttribute
import lexicon.*

object ConcreteTransformer {

    fun toAttribute(def: SchemaDef.Concrete, isNullable: Boolean, name: String): KtAttribute<*> {
        return when (def) {
            is LexiconBlob -> toAttribute(def, isNullable, name)
            is LexiconBoolean -> toAttribute(def, isNullable, name)
            is LexiconBytes -> toAttribute(def, isNullable, name)
            is LexiconCidLink -> toAttribute(def, isNullable, name)
            is LexiconInteger -> toAttribute(def, isNullable, name)
            is LexiconNull -> toAttribute(def, isNullable, name)
            is LexiconString -> toAttribute(def, isNullable, name)
            else -> throw IllegalArgumentException("TODO")
        }
    }

    private fun toAttribute(def: LexiconBlob, isNullable: Boolean, name: String): KtAttribute<*> {
        TODO()
    }

    private fun toAttribute(def: LexiconBoolean, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = Boolean::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }

    private fun toAttribute(def: LexiconBytes, isNullable: Boolean, name: String): KtAttribute<*> {
        TODO()
    }

    private fun toAttribute(def: LexiconCidLink, isNullable: Boolean, name: String): KtAttribute<*> {
        TODO()
    }

    private fun toAttribute(def: LexiconInteger, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = Int::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }

    private fun toAttribute(def: LexiconNull, isNullable: Boolean, name: String): KtAttribute<*> {
        TODO()
    }

    private fun toAttribute(def: LexiconString, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = String::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }
}