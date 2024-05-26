package codegen

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.*

// TODO all need toType()

// TODO use ByteArray instead of String?
object LexiconBlobConverter: ParameterConverter<LexiconBlob> {
    override fun toParameter(def: LexiconBlob, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }
}

// TODO use ByteArray instead of String?
object LexiconBytesConverter: ParameterConverter<LexiconBytes> {
    override fun toParameter(def: LexiconBytes, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }
}

object LexiconBooleanConverter: ParameterConverter<LexiconBoolean>, PropertyConverter<LexiconBoolean> {

    override fun toParameter(def: LexiconBoolean, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(Boolean::class, def.default, isNullable, name)
    }

    override fun toProperty(def: LexiconBoolean, name: String): PropertySpec {
        return buildProperty(Boolean::class, name, def.const)
    }
}

object LexiconCidLinkConverter: ParameterConverter<LexiconCidLink> {
    override fun toParameter(def: LexiconCidLink, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }
}

object LexiconIntegerConverter: ParameterConverter<LexiconInteger>, PropertyConverter<LexiconInteger> {

    override fun toParameter(def: LexiconInteger, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(Int::class, def.default, isNullable, name)
    }

    override fun toProperty(def: LexiconInteger, name: String): PropertySpec {
        return buildProperty(Int::class, name, def.const)
    }
}

// TODO null all

object LexiconStringConverter: ParameterConverter<LexiconString>, PropertyConverter<LexiconString>, TypeConverter<LexiconString> {

    override fun toParameter(def: LexiconString, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, def.default, isNullable, name)
    }

    override fun toProperty(def: LexiconString, name: String): PropertySpec {
        return buildProperty(String::class, name, def.const)
    }

    override fun toType(def: LexiconString, name: String): TypeSpec {
        return buildValueClass(String::class, name)
    }
}