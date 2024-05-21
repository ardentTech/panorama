package codegen

import lexicon.*

internal fun LexiconArray.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<List<*>> {
    return KConstructorPropertyConfig(
        cls = List::class,
        isNullable = isNullable,
        itemCls = when(this.items) {
            is LexiconBlob -> String::class
            is LexiconBoolean -> Boolean::class
            is LexiconBytes -> String::class
            is LexiconCidLink -> String::class // TODO not sure this is what i want
            is LexiconInteger -> Int::class
            is LexiconRef -> String::class
            is LexiconString -> String::class
            is LexiconUnion -> String::class
            is LexiconUnknown -> String::class
        },
        name = name
    )
}