package codegen

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.*

object LexiconArrayConverter: ParameterConverter<LexiconArray> {
    override fun toParameter(def: LexiconArray, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(List::class, null, isNullable, name)
    }
}

object LexiconObjectConverter: TypeConverter<LexiconObject> {

    override fun toType(def: LexiconObject, name: String): TypeSpec {
        val properties = mutableListOf<PropertySpec>()
        val parameters = mutableListOf<ParameterSpec>()

        def.properties.map { (k, v) ->
            when (v) {
                is LexiconArray -> parameters.add(LexiconArrayConverter.toParameter(v, def.isNullable(k), k))
                is LexiconBlob -> parameters.add(LexiconBlobConverter.toParameter(v, def.isNullable(k), k))
                is LexiconBoolean -> {
                    if (v.const != null) {
                        properties.add(LexiconBooleanConverter.toProperty(v, k))
                    } else {
                        parameters.add(LexiconBooleanConverter.toParameter(v, def.isNullable(k), k))
                    }
                }
                is LexiconBytes -> parameters.add(LexiconBytesConverter.toParameter(v, def.isNullable(k), k))
                is LexiconCidLink -> parameters.add(LexiconCidLinkConverter.toParameter(v, def.isNullable(k), k))
                is LexiconInteger -> {
                    if (v.const != null) {
                        properties.add(LexiconIntegerConverter.toProperty(v, k))
                    } else {
                        parameters.add(LexiconIntegerConverter.toParameter(v, def.isNullable(k), k))
                    }
                }
                is LexiconRef -> parameters.add(LexiconRefConverter.toParameter(v, def.isNullable(k), k))
                is LexiconString -> {
                    if (v.const != null) {
                        properties.add(LexiconStringConverter.toProperty(v, k))
                    } else {
                        parameters.add(LexiconStringConverter.toParameter(v, def.isNullable(k), k))
                    }
                }
                is LexiconUnion -> parameters.add(LexiconUnionConverter.toParameter(v, def.isNullable(k), k))
                is LexiconUnknown -> parameters.add(LexiconUnknownConverter.toParameter(v, def.isNullable(k), k))
                else -> throw IllegalArgumentException("Unsupported property type: ${v::class.simpleName}")
            }
        }

        return buildDataClass(def.description, name, parameters, properties)
    }
}

// TODO params