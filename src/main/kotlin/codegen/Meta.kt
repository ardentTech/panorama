package codegen

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import lexicon.LexiconRef
import lexicon.LexiconToken
import lexicon.LexiconUnion
import lexicon.LexiconUnknown

// TODO resolve
object LexiconRefConverter: ParameterConverter<LexiconRef>, TypeConverter<LexiconRef> {
    override fun toParameter(def: LexiconRef, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }

    override fun toType(def: LexiconRef, name: String): TypeSpec {
        TODO("Not yet implemented")
    }
}

object LexiconTokenConverter: TypeConverter<LexiconToken> {
    override fun toType(def: LexiconToken, name: String): TypeSpec {
        return buildDataObject(def.description, name, listOf(
            buildProperty(String::class, "token", name.uncapitalize())
        ))
    }
}

// TODO resolve
object LexiconUnionConverter: ParameterConverter<LexiconUnion>, TypeConverter<LexiconUnion> {
    override fun toParameter(def: LexiconUnion, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }

    override fun toType(def: LexiconUnion, name: String): TypeSpec {
        TODO("Not yet implemented")
    }
}

// TODO decide how to handle this
object LexiconUnknownConverter: ParameterConverter<LexiconUnknown> {
    override fun toParameter(def: LexiconUnknown, isNullable: Boolean, name: String): ParameterSpec {
        return buildParameter(String::class, null, isNullable, name)
    }
}