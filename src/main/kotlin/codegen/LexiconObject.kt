package codegen

import com.squareup.kotlinpoet.*
import lexicon.*

internal fun LexiconObject.codegen(name: String): TypeSpec {
    val propConfigs = this.properties.map { (key, value) -> KPropConfig.from(value, this.nullable?.contains(key) == true, key) }
    return generateKDataClass(
        KDataClassConfig(
            bodyProperties = propConfigs.filter { it.constantValue != null },
            constructorProperties = propConfigs.filter { it.constantValue == null },
            description = this.description,
            name = name
        )
    )
}