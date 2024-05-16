package codegen

import com.squareup.kotlinpoet.*
import lexicon.LexiconToken
import java.util.*

fun LexiconToken.codegen(className: String): TypeSpec {
    val spec = TypeSpec.objectBuilder(className)
        .addProperty(
            PropertySpec.builder("code", String::class)
                .initializer("%S", className.replaceFirstChar {
                    // TODO shouldn't need to do this so rework for LexiconDoc.codegen passes down info
                    if (it.isUpperCase()) it.lowercase(Locale.getDefault()) else it.toString()
                })
                .build()
        )

    this.description?.let { spec.addKdoc(it) }

    return spec.build()
}