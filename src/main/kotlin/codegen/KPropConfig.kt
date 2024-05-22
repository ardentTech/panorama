package codegen

import lexicon.*
import kotlin.reflect.KClass

data class KPropConfig<T: Any>(
    val cls: KClass<T>,
    val constantValue: T? = null,
    val defaultValue: T? = null,
    val isNullable: Boolean = false,
    val itemCls: KClass<out Any>? = null,
    val name: String,
    val validators: List<String> = emptyList()
) {
    init {
        constantValue?.let { require(defaultValue == null) }
        defaultValue?.let { require(constantValue == null) }
        itemCls?.let { require(cls == List::class) }
        if (cls == List::class) require(itemCls != null)
    }
}

object KPropConfigMapper {
    fun from(def: LexiconObject.Property, isNullable: Boolean = false, name: String): KPropConfig<*> = when(def) {
        is LexiconArray -> from(def, isNullable, name)
        is LexiconBlob -> from(def, isNullable, name)
        is LexiconBoolean -> KPropConfig(Boolean::class, def.const, def.default, isNullable, null, name)
        is LexiconBytes -> from(def, isNullable, name)
        is LexiconCidLink -> KPropConfig(Cid::class, null, null, isNullable, null, name)
        is LexiconInteger -> from(def, isNullable, name)
        is LexiconRef -> KPropConfig(String::class, null, null, isNullable, null, name)
        is LexiconString -> from(def, isNullable, name)
        is LexiconUnion -> KPropConfig(String::class, null, null, isNullable, null, name)
        is LexiconUnknown -> KPropConfig(Any::class, null, null, isNullable, null, name)
    }

    fun from(def: LexiconParams.Property, name: String): KPropConfig<*> = when(def) {
        is LexiconArray -> from(def, false, name)
        is LexiconBoolean -> KPropConfig(Boolean::class, def.const, def.default, false, null, name)
        is LexiconInteger -> from(def, false, name)
        is LexiconString -> from(def, false, name)
        is LexiconUnknown -> KPropConfig(Any::class, null, null, false, null, name)
    }

    private fun from(def: LexiconArray, isNullable: Boolean = false, name: String): KPropConfig<List<*>> {
        val itemCls = when(def.items) {
            is LexiconBlob -> String::class
            is LexiconBoolean -> Boolean::class
            is LexiconBytes -> String::class
            is LexiconCidLink -> Cid::class // TODO is this correct?
            is LexiconInteger -> Int::class
            is LexiconRef -> String::class
            is LexiconString -> String::class
            is LexiconUnion -> String::class
            is LexiconUnknown -> String::class
        }
        return KPropConfig(List::class, null, null, isNullable, itemCls, name, emptyList())
    }

    private fun from(def: LexiconBlob, isNullable: Boolean = false, name: String): KPropConfig<String> {
        val validators = mutableListOf<String>()
        def.accept?.let { validators.add(KPropValidators.membership(name, it)) }
        def.maxSize?.let { validators.add(KPropValidators.lte(name.toByteArray().count(), it)) }
        return KPropConfig(String::class, null, null, isNullable, null, name, validators)
    }

    private fun from(def: LexiconBytes, isNullable: Boolean = false, name: String): KPropConfig<String> {
        val validators = mutableListOf<String>()
        def.maxLength?.let { validators.add(KPropValidators.lte(name.count(), it)) }
        def.minLength?.let { validators.add(KPropValidators.gte(name.count(), it)) }
        return KPropConfig(String::class, null, null, isNullable, null, name, validators)
    }

    private fun from(def: LexiconInteger, isNullable: Boolean = false, name: String): KPropConfig<Int> {
        val validators = mutableListOf<String>()
        def.enum?.let { validators.add(KPropValidators.membership(name, it))}
        def.maximum?.let { validators.add(KPropValidators.lte(name, it)) }
        def.minimum?.let { validators.add(KPropValidators.gte(name, it)) }
        return KPropConfig(Int::class, def.const, def.default, isNullable, null, name, validators)
    }

    private fun from(def: LexiconString, isNullable: Boolean = false, name: String): KPropConfig<String> {
        val validators = mutableListOf<String>()
        def.enum?.let { validators.add(KPropValidators.membership(name, it)) }
        def.maxLength?.let { validators.add(KPropValidators.lte(name.count(), it)) }
        def.minLength?.let { validators.add(KPropValidators.gte(name.count(), it)) }
        return KPropConfig(String::class, def.const, def.default, isNullable, null, name, validators)
    }
}
