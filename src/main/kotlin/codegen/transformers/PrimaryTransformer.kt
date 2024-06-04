package codegen.transformers

import codegen.KtType
import lexicon.*

object PrimaryTransformer {

    fun toTypes(def: LexiconProcedure, name: String): List<KtType> {
        val types = mutableListOf<KtType>()
        def.input?.let { toType(it, "${name}Input")?.let { schema -> types.add(schema) } }
        def.output?.let { toType(it, "${name}Output")?.let { schema -> types.add(schema) } }
        def.errors?.let { types.add(toType(it, "${name}Error")) }
        return types
    }

    fun toTypes(def: LexiconQuery, name: String): List<KtType> {
        val types = mutableListOf<KtType>()
        def.parameters?.let { ContainerTransformer.toType(it, "${name}Parameters").let { schema -> types.add(schema) } }
        def.output?.let { toType(it, "${name}Output")?.let { schema -> types.add(schema) } }
        def.errors?.let { types.add(toType(it, "${name}Error")) }
        return types
    }

    fun toType(def: LexiconRecord, name: String): KtType {
        TODO()
    }

    fun toTypes(def: LexiconSubscription, name: String): List<KtType> {
        val types = mutableListOf<KtType>()
        def.parameters?.let { ContainerTransformer.toType(it, "${name}Parameters").let { schema -> types.add(schema) } }
        def.message?.let { toType(it, "${name}Message") }
        def.errors?.let { types.add(toType(it, "${name}Error")) }
        return types
    }

    private fun toType(def: LexiconSubscription.Message, name: String): KtType? {
        // TODO handle description and union schema
        return null
    }

    private fun toType(def: PrimaryIO, name: String): KtType? {
        return def.schema?.let { toType(it, name) }
    }

    private fun toType(def: PrimaryIOSchema, name: String): KtType {
        return when (def) {
            is LexiconObject -> ContainerTransformer.toType(def, name)
            is LexiconRef -> MetaTransformer.toTypeAlias(def, name)
            is LexiconUnion -> MetaTransformer.toTypeAlias(def, name)
        }
    }

    private fun toType(def: List<PrimaryError>, name: String): KtType.KtEnum {
        return KtType.KtEnum(def.map { it.name }, null, name)
    }
}