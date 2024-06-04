package codegen.transformers

import codegen.KtAttribute
import codegen.KtType
import lexicon.LexiconRef
import lexicon.LexiconToken
import lexicon.LexiconUnion

object MetaTransformer {

    fun toTypeAlias(def: LexiconRef, name: String) = KtType.KtTypeAlias(name, String::class) // TODO `this.ref` to type reference
    fun toAttribute(def: LexiconRef, isNullable: Boolean, name: String) = KtAttribute.KtParameter.KtReference(isNullable, name, def.ref)

    fun toDataObject(def: LexiconToken, name: String) = KtType.KtDataObject(def.description, name)

    fun toTypeAlias(def: LexiconUnion, name: String) = KtType.KtTypeAlias(name, String::class) // TODO this is a placeholder
    fun toAttribute(def: LexiconUnion, isNullable: Boolean, name: String) = KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name) // TODO

    // unknown
}