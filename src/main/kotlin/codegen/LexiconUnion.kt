package codegen

import com.squareup.kotlinpoet.ClassName
import lexicon.LexiconUnion

// TODO this is pseudo-code
fun LexiconUnion.toPropertyConfig(keyName: String): PropertyConfig<String> {
    return PropertyConfig(
        cls = String::class,
        const = null,
        default = null,
        //typeName = ClassName("", this.refs.first().javaClass.kotlin.qualifiedName!!)
    )
}