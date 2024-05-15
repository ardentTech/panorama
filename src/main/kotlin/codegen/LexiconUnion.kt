package codegen

import com.squareup.kotlinpoet.ClassName
import lexicon.LexiconUnion

// TODO this is pseudo-code
fun LexiconUnion.toPropertyConfig(): PropertyConfig<String> {
    return PropertyConfig(
        const = null,
        default = null,
        typeName = ClassName("", this.refs.first().javaClass.kotlin.qualifiedName!!)
    )
}