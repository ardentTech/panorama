package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconRecord

// TODO implement
fun LexiconRecord.codegen(name: String): TypeSpec {
    return generateKDataClass(
        KDataClassConfig(
            bodyProperties = listOf(),
            constructorProperties = listOf(), // invalid
            description = this.description,
            name = name
        )
    )
}