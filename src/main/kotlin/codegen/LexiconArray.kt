package codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconArray

fun LexiconArray.toPropertyConfig(): PropertyConfig<String> {
    return PropertyConfig(
        const = null,
        default = null,
        typeName = List::class.asTypeName().parameterizedBy(
            ClassName("", this.items.javaClass.kotlin.qualifiedName!!)
        )
    )
}