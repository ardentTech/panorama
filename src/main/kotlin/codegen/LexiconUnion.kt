package codegen

import lexicon.LexiconUnion

// TODO this is pseudo-code
internal fun LexiconUnion.toPropertyConfig(name: String, isNullable: Boolean = false): KConstructorPropertyConfig<String> {
    return KConstructorPropertyConfig(
        cls = String::class,
        isNullable = isNullable,
        name = name
    )
}