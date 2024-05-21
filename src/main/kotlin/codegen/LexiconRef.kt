package codegen

import com.squareup.kotlinpoet.*
import lexicon.LexiconRef

// TODO this should refer to another data class (the func body below is pseudo-code)
// TODO use a typealias? https://square.github.io/kotlinpoet/type-aliases/
fun LexiconRef.codegen(name: String): TypeSpec {
    return TypeSpec.dataclass(name)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter("foo", String::class)
                .build())
        .addProperty(
            PropertySpec.builder("foo", String::class)
                .initializer("foo")
                .build())
        .build()
}

// TODO this should nest another data class
internal fun LexiconRef.toPropertyConfig(name: String, isNullable: Boolean = false): KConstructorPropertyConfig<String> {
    return KConstructorPropertyConfig(
        cls = String::class,
        isNullable = isNullable,
        name = name,
    )
}