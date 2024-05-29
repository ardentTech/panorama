package codegen.transformers

import codegen.*
import codegen.capitalize
import lexicon.*

internal object LexiconDocTransformer {

    fun toFile(doc: LexiconDoc): KtFile {
        var description = doc.description
        val types = mutableListOf<KtType>()

        doc.defs.forEach { (key, def) ->
            if (key == "main") description = def.description
            types += toTypes(def, getDefName(doc.name, key))
        }
        return KtFile(types, getComment(description, doc.id, doc.revision, doc.lexicon), doc.namespace ?: "", doc.name.capitalize())
    }

    private fun getComment(description: String?, id: String, revision: Int?, version: Int) =
        """
            |This file was generated by Panorama. DO NOT EDIT.
            |
            |Lexicon: $id
            |Version: $version
            |Revision: ${revision ?: "N/A"}
            |Description: ${description ?: "N/A"}
            |
        """.trimMargin()

    private fun getDefName(lexiconName: String, propertyKey: String) =
        if (propertyKey == "main") lexiconName.capitalize()
        else if (lexiconName == "defs") propertyKey.capitalize()
        else "${lexiconName}${propertyKey.capitalize()}"

    // these calls should all be `toType` or `toTypes`
    private fun toTypes(def: SchemaDef, name: String) = when (def) {
        is LexiconArray -> listOf(ContainerTransformer.toType(def, name))
        // blob
        // boolean
        // bytes
        // cid-link
        // integer
        is LexiconObject -> listOf(ContainerTransformer.toType(def, name))
        is LexiconProcedure -> PrimaryTransformer.toTypes(def, name)
        is LexiconQuery -> PrimaryTransformer.toTypes(def, name)
        // record
        // ref
        // string
        // subscription
        // token
        // union
        // unknown
        else -> throw IllegalArgumentException("Unsupported type: ${def::class.simpleName}")
    }
}