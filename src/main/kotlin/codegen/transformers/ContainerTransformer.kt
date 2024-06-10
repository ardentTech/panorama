package codegen.transformers

import codegen.KtAttribute
import codegen.KtType
import lexicon.*

object ContainerTransformer {

    fun toType(def: LexiconArray, name: String): KtType {
        return KtType.KtCollection(
            cls = List::class,
            description = def.description,
            itemCls = when (def.items) {
                is LexiconBlob -> String::class
                is LexiconBoolean -> Boolean::class
                is LexiconBytes -> String::class
                is LexiconCidLink -> String::class
                is LexiconInteger -> Int::class
                is LexiconRef -> String::class
                is LexiconString -> String::class
                is LexiconUnion -> String::class
                is LexiconUnknown -> String::class
            },
            name = name
        )
    }

    fun toType(def: LexiconObject, name: String): KtType {
        return KtType.KtDataClass(
            def.properties.map { (k, v) -> toAttribute(v, def.nullable?.contains(k) == true, k) },
            def.description,
            name,
        )
    }

    fun toType(def: LexiconParams, name: String): KtType {
        return KtType.KtDataClass(
            def.properties.map { (k, v) -> toAttribute(v, k) },
            def.description,
            name,
        )
    }

    private fun toAttribute(def: LexiconArray, isNullable: Boolean, name: String): KtAttribute<*> {
        val itemCls = when(def.items) {
            // TODO these aren't all valid
            is LexiconBlob -> String::class
            is LexiconBoolean -> Boolean::class
            is LexiconBytes -> String::class
            is LexiconCidLink -> String::class
            is LexiconInteger -> Int::class
            is LexiconRef -> String::class // TODO how to handle this?
            is LexiconString -> String::class
            is LexiconUnion -> String::class
            is LexiconUnknown -> String::class
        }
        return KtAttribute.KtParameter.KtCollection(List::class, null, isNullable, itemCls, name)
    }

    private fun toAttribute(def: LexiconObject.Property, isNullable: Boolean, name: String): KtAttribute<*> {
        return when (def) {
            is SchemaDef.Concrete -> ConcreteTransformer.toAttribute(def, isNullable, name)
            is LexiconArray -> toAttribute(def, isNullable, name)
            // this will need to have access to any records already created
            // i'm not sure how this will fit into KtAttribute
            is LexiconRef -> MetaTransformer.toAttribute(def, isNullable, name)
            is LexiconUnion -> MetaTransformer.toAttribute(def, isNullable, name)
            is LexiconUnknown -> TODO("generics")
        }
    }

    private fun toAttribute(def: LexiconParams.Property, name: String): KtAttribute<*> {
        return when (def) {
            is LexiconArray -> toAttribute(def, false, name)
            is LexiconBoolean -> ConcreteTransformer.toAttribute(def, false, name)
            is LexiconInteger -> ConcreteTransformer.toAttribute(def, false, name)
            is LexiconString -> ConcreteTransformer.toAttribute(def, false, name)
            is LexiconUnknown -> TODO("generics")
        }
    }
}