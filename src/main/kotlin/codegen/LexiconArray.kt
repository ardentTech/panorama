package codegen

import lexicon.*

fun LexiconArray.toPropertyConfig(keyName: String): PropertyConfig<List<*>> {
    return PropertyConfig(
        cls = List::class,
        const = null,
        default = null,
        // TODO should this map to Kotlin primitives instead of Lexicon<...> types?
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
        }
    )
}