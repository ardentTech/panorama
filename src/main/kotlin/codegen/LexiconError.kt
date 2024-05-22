package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconError

fun List<LexiconError>.codegen(name: String): TypeSpec {
    return generateKEnum(name, this.map { it.name.camelToEnumCase() })
}
