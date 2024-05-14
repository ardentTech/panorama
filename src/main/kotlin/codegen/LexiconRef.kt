package codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import lexicon.LexiconRef

// TODO this should nest another data class
fun LexiconRef.toPropertyConfig(): PropertyConfig<String> {
    return PropertyConfig(
        const = null,
        default = null,
        typeName = ClassName("", this.ref.javaClass.kotlin.qualifiedName!!)
    )
}

// TODO this should refer to another data class (the func body below is pseudo-code)
fun LexiconRef.codegen(name: String): TypeSpec {
    val spec = TypeSpec.dataclass(name)
    val constructorBuilder = FunSpec.constructorBuilder()
    constructorBuilder.addParameter("foo", String::class.asTypeName())
    return spec.build()
}