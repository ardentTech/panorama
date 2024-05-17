package codegen

import com.squareup.kotlinpoet.asTypeName
import lexicon.Cid
import lexicon.LexiconCidLink

fun LexiconCidLink.toPropertyConfig(keyName: String): PropertyConfig<Cid> {
    return PropertyConfig(
        cls = Cid::class,
        const = null,
        default = null,
        //typeName = Cid::class.asTypeName(),
        validators = emptyList(),
    )
}