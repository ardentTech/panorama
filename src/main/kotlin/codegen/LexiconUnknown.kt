package codegen

import lexicon.LexiconUnknown

// TODO does this need a more specific type than Any?
// atrium maps this to an `Ipld` struct: https://github.com/sugyan/atrium/blob/7768c36887b24a990b3d200895644a711ef5fb04/atrium-api/src/types.rs#L100
// Indicates than any data could appear at this location, with no specific validation.
internal fun LexiconUnknown.toPropertyConfig(name: String, isNullable: Boolean = false): KPropertyConfig<Any> {
    return KConstructorPropertyConfig(
        cls = Any::class,
        isNullable = isNullable,
        name = name
    )
}