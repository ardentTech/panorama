package codegen

import com.squareup.kotlinpoet.TypeSpec
import lexicon.*

internal fun LexiconParams.codegen(name: String): TypeSpec {
    val propConfigs = this.properties.map { (key, value) -> KPropConfigMapper.from(value, key) }
    return generateKDataClass(
        KDataClassConfig(
            bodyProperties = propConfigs.filter { it.constantValue != null },
            constructorProperties = propConfigs.filter { it.constantValue == null },
            description = this.description,
            name = name
        )
    )
}