package codegen.transformers

import codegen.KtAttribute
import codegen.KtType
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
        }
    }

    // convert string knownValues to comment?
    fun toType(def: LexiconString, name: String): KtType {
        return KtType.KtValueClass(
            name = name,
            parameter = KtAttribute.KtParameter.KtItem(
                cls = String::class,
                default = def.default,
                isNullable = false,
                name = "s"
            ),
        )
    }

    private fun toAttribute(def: LexiconBlob, isNullable: Boolean, name: String): KtAttribute<*> {
        // TODO(implement)
        return KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name)
    }

    private fun toAttribute(def: LexiconBoolean, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = Boolean::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }

    private fun toAttribute(def: LexiconBytes, isNullable: Boolean, name: String): KtAttribute<*> {
        // TODO(implement)
        return KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name)
    }

    private fun toAttribute(def: LexiconCidLink, isNullable: Boolean, name: String): KtAttribute<*> {
        // TODO(implement)
        return KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name)
    }

    private fun toAttribute(def: LexiconInteger, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = Int::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }

    private fun toAttribute(def: LexiconNull, isNullable: Boolean, name: String): KtAttribute<*> {
        // TODO(implement)
        return KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name)
    }

    private fun toAttribute(def: LexiconString, isNullable: Boolean, name: String): KtAttribute<*> {
        val cls = String::class
        return def.const?.let { constant ->
            KtAttribute.KtProperty.KtItem(cls, constant, isNullable, name)
        } ?: KtAttribute.KtParameter.KtItem(cls, def.default, isNullable, name)
    }
}