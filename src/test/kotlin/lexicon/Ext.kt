package lexicon

fun LexiconDoc.Companion.factory(
    defs: Map<String, SchemaDef>,
    description: String? = null,
    id: String = "com.atproto.foo.bar",
    lexicon: Int = 1,
    revision: Int? = null
) = LexiconDoc(
    defs = defs,
    description = description,
    id = id,
    lexicon = lexicon,
    revision = revision
)

fun LexiconObject.Companion.factory(
    description: String? = null,
    nullable: List<String>? = null,
    properties: Map<String, LexiconObject.Property> = emptyMap(),
    required: List<String>? = null
) = LexiconObject(
    description, nullable, properties, required
)

fun LexiconParams.Companion.factory(
    description: String? = null,
    properties: Map<String, LexiconParams.Property> = emptyMap(),
    required: List<String>? = null
) = LexiconParams(
    description, properties, required
)