package codegen

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconRecord

fun LexiconRecord.codegen(name: String): TypeSpec {
    val spec = TypeSpec.dataclass(name)

    // TODO description

    spec.primaryConstructor(
        FunSpec.constructorBuilder()
            .addParameter("key", String::class) // TODO should be based on this.key
            .build()
    )
    spec.addProperty(
        PropertySpec.builder("key", String::class)
            .initializer("key")
            .build()
    )

    return spec.build()
}