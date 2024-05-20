package codegen

import com.squareup.kotlinpoet.*
import lexicon.LexiconToken

fun LexiconToken.codegen(className: String): TypeSpec {

    return generateKObject(
        KObjectConfig(
            description = this.description,
            name = className,
            properties = listOf(
                KBodyPropertyConfig(
                    cls = String::class,
                    name = "code",
                    value = className.uncapitalize(),
                )
            )
        )
    )
}