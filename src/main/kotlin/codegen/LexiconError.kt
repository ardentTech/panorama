package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconError
import java.util.*

fun String.camelToEnumCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "_$0").uppercase(Locale.getDefault())
}

fun List<LexiconError>.codegen(name: String): TypeSpec {
    return generateKEnum(name, this.map { it.name.camelToEnumCase() })
}
