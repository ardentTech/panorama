package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.Cid
import lexicon.LexiconCidLink

internal fun LexiconCidLink.toPropertyConfig(name: String, isNullable: Boolean = false): KConstructorPropertyConfig<Cid> {
    return KConstructorPropertyConfig(
        cls = Cid::class,
        isNullable = isNullable,
        name = name,
        validators = emptyList(),
    )
}