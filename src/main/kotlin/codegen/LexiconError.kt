package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconError
import java.util.*

fun String.camelToEnumCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "_$0").uppercase(Locale.getDefault())
}

fun List<LexiconError>.codegen(name: String): TypeSpec {
    val spec = TypeSpec.enumBuilder("${name}Errors")
    this.forEach { error ->
        spec.addEnumConstant(error.name.camelToEnumCase())
    }
    return spec.build()
}
