package codegen

import lexicon.*
import java.util.*

// convert lexicon types to kt types

internal fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase(Locale.getDefault()) else it.toString()
    }
}

internal fun LexiconDoc.toFile(): KtFile {
    var description: String? = null
    val types = mutableListOf<KtType>()

    defs.map { (key, def) ->
        val name = if (key == "main") {
            description = def.description
            this.name.capitalize()
        } else if (this.name == "defs") key.capitalize()
        else "${this.name}${key.capitalize()}"

        when (def) {
            // array
            // blob
            // boolean
            // bytes
            // cid-link
            // integer
            is LexiconObject -> types.add(def.toType(name))
            is LexiconProcedure -> types += def.toTypes(name)
            is LexiconQuery -> types += def.toTypes(name)
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

    return KtFile(
        types,
    """
        |This file was generated by Panorama. DO NOT EDIT.
        |
        |Lexicon: ${this.id}
        |Version: ${this.lexicon}
        |Revision: ${this.revision ?: "N/A"}
        |Description: ${description ?: "N/A"}
        |
    """.trimMargin(),
        this.namespace ?: "",
        this.name.capitalize()
    )
}

// CONCRETE

// blob

internal fun LexiconBoolean.toAttribute(isNullable: Boolean, name: String): KtAttribute<*> {
    return this.const?.let { constant ->
        KtAttribute.KtProperty.KtItem(Boolean::class, constant, isNullable, name)
    } ?: KtAttribute.KtParameter.KtItem(Boolean::class, default, isNullable, name)
}

// bytes

// cid-link

internal fun LexiconInteger.toAttribute(isNullable: Boolean, name: String): KtAttribute<*> {
    return this.const?.let { constant ->
        KtAttribute.KtProperty.KtItem(Int::class, constant, isNullable, name)
    } ?: KtAttribute.KtParameter.KtItem(Int::class, default, isNullable, name)
}

// null

// TODO unprocessed LexiconString members (e.g. format)
internal fun LexiconString.toAttribute(isNullable: Boolean, name: String): KtAttribute<*> {
    return this.const?.let { constant ->
        KtAttribute.KtProperty.KtItem(String::class, constant, isNullable, name)
    } ?: KtAttribute.KtParameter.KtItem(String::class, default, isNullable, name)
}

// CONTAINER

internal fun LexiconArray.toAttribute(isNullable: Boolean, name: String): KtAttribute<*> {
    val itemCls = when(items) {
        // TODO these aren't all valid
        is LexiconBlob -> String::class
        is LexiconBoolean -> Boolean::class
        is LexiconBytes -> String::class
        is LexiconCidLink -> String::class
        is LexiconInteger -> Int::class
        is LexiconRef -> String::class // TODO how to handle this?
        is LexiconString -> String::class
        is LexiconUnion -> String::class
        is LexiconUnknown -> String::class
    }
    return KtAttribute.KtParameter.KtCollection(List::class, null, isNullable, itemCls, name)
}

internal fun LexiconObject.Property.toAttribute(isNullable: Boolean, name: String): KtAttribute<*> {
    return when (this) {
        is LexiconArray -> this.toAttribute(isNullable, name)
        // blob
        is LexiconBoolean -> this.toAttribute(isNullable, name)
        // bytes
        // cid-link
        is LexiconInteger -> this.toAttribute(isNullable, name)
        
        // this will need to have access to any records already created
        // i'm not sure how this will fit into KtAttribute
        is LexiconRef -> KtAttribute.KtParameter.KtReference(isNullable, name, this.ref)
        //is LexiconRef -> KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name) // TODO
        
        is LexiconString -> this.toAttribute(isNullable, name)
        is LexiconUnion -> KtAttribute.KtParameter.KtItem(String::class, null, isNullable, name) // TODO
        // unknown
        else -> throw IllegalArgumentException("Cannot map LexiconObject.Property ${this::class.simpleName} to KtMember")
    }
}

internal fun LexiconParams.Property.toAttribute(name: String): KtAttribute<*> {
    return when (this) {
        is LexiconArray -> this.toAttribute(false, name)
        is LexiconBoolean -> this.toAttribute(false, name)
        is LexiconInteger -> this.toAttribute(false, name)
        is LexiconString -> this.toAttribute(false, name)
        // unknown
        else -> throw IllegalArgumentException("Cannot map LexiconParams.Property ${this::class.simpleName} to KtMember")
    }
}

internal fun LexiconParams.toType(name: String): KtType {
    return KtType.KtDataClass(
        this.description,
        this.properties.map { (key, value) -> value.toAttribute(key) },
        name,
    )
}

// META

// token
// ref
// union
// unknown

// PRIMARY

internal fun LexiconProcedure.toTypes(name: String): List<KtType> {
    val types = mutableListOf<KtType>()

    input?.let { io ->
        io.toType("${name}Input")?.let { schema -> types.add(schema) }
    }
    output?.let { io ->
        io.toType("${name}Output")?.let { schema -> types.add(schema) }
    }
//    errors?.let {  }
    return types
}

internal fun LexiconQuery.toTypes(name: String): List<KtType> {
    val types = mutableListOf<KtType>()

    parameters?.let { types.add(it.toType("${name}Parameters")) }
    output?.let { io ->
        io.toType("${name}Output")?.let { schema -> types.add(schema) }
    }
//    errors?.let {  }
    return types
}

internal fun PrimaryIOSchema.toType(name: String): KtType {
    return when (this) {
        is LexiconObject -> {
            // TODO might need to move this to its own function
            KtType.KtDataClass(
                this.description,
                this.properties.map { (key, value) -> value.toAttribute(nullable?.contains(key) == true, key) },
                name,
            )
        }
        is LexiconRef -> KtType.KtTypeAlias(name, String::class) // TODO `this.ref` to type reference
        is LexiconUnion -> KtType.KtTypeAlias(name, String::class) // TODO this is a placeholder
    }
}

internal fun PrimaryIO.toType(name: String): KtType? {
    return this.schema?.toType(name)
}