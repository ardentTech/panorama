package codegen

import com.squareup.kotlinpoet.*
import lexicon.LexiconRef

// TODO this should refer to another data class (the func body below is pseudo-code)
// TODO use a typealias? https://square.github.io/kotlinpoet/type-aliases/
fun LexiconRef.codegen(name: String): TypeSpec {
    //val demo = TypeAliasSpec.builder("Word", String::class).build()
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
fun LexiconRef.toPropertyConfig(keyName: String): PropertyConfig<String> {
    return PropertyConfig(
        cls = String::class,
        const = null,
        default = null,
        //typeName = ClassName("", this.ref.javaClass.kotlin.qualifiedName!!)
    )
}