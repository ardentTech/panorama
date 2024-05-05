package lexicon

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private object LexiconStringFormat: KSerializer<LexiconString.Format> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("lexicon.LexiconString.Format", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LexiconString.Format {
        return decoder.decodeString().let { code ->
            LexiconString.Format.entries.firstOrNull { it.code == code } ?: run {
                throw IllegalArgumentException("Unexpected format: $code")
            }
        }
    }

    override fun serialize(encoder: Encoder, value: LexiconString.Format) = encoder.encodeString(value.code)
}

@Serializable
data class LexiconString(
    val const: String? = null,
    val default: String? = null,
    override val description: String? = null,
    val enum: List<String>? = null,
    @Serializable(with = LexiconStringFormat::class) val format: Format? = null,
    val maxLength: Int? = null,
    val minLength: Int? = null,
    val maxGraphemes: Int? = null,
    val minGraphemes: Int? = null,
    val knownValues: List<String>? = null,
): SchemaDef.FieldType, LexiconArray.Items, LexiconObject.Property, LexiconParams.Property {

    @Serializable
    enum class Format(val code: String) {
        AT_IDENTIFIER("at-identifier"),
        AT_URI("at-uri"),
        CID("cid"),
        DATETIME("datetime"),
        DID("did"),
        HANDLE("handle"),
        LANGUAGE("language"),
        NSID("nsid"),
        RECORD_KEY("record-key"),
        TID("tid"),
        URI("uri"),
    }

    override val type = LexiconType.STRING
}