import codegen.codegen
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import lexicon.*
import java.nio.file.Path
import kotlin.Exception
import kotlin.io.path.*

private val logger = KotlinLogging.logger {}
private const val JSON_FILE_EXT = "json"
private const val LEXICON_SOURCE_DIR = "./data"
private const val SERIALIZER_CLASS_DISCRIMINATOR = "discriminator"

object Panorama {

    private val json by lazy {
        Json {
            classDiscriminator = SERIALIZER_CLASS_DISCRIMINATOR
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }

    fun codegen(doc: LexiconDoc): Result<Unit> {
        doc.codegen().writeTo(System.out)
        return Result.success(Unit)
    }

    fun codegen(doc: LexiconDoc, destination: Path): Result<Unit> {
        doc.codegen().writeTo(destination)
        return Result.success(Unit)
    }

    @OptIn(ExperimentalPathApi::class)
    fun crawl(namespaces: List<String> = emptyList(), source: Path = Path(LEXICON_SOURCE_DIR)): Result<List<LexiconDoc>> =
        try {
            val docs = source.walk(PathWalkOption.INCLUDE_DIRECTORIES).filter { it.extension == JSON_FILE_EXT }.map {
                json.decodeFromString<LexiconDoc>(it.readText())
            }.toList()
            Result.success(if (namespaces.isNotEmpty()) { docs.filter { it.namespace in namespaces }} else { docs })
        } catch (e: Exception) {
            Result.failure(e)
        }
}

fun main() {
    Panorama.crawl(namespaces = listOf(
//        "com.atproto.admin",
//        "com.atproto.identity",
//        "com.atproto.label",
//        "com.atproto.moderation",
//        "com.atproto.repo",
//        "com.atproto.server",
//        "com.atproto.sync",
//        "com.atproto.temp",
//        "app.bsky.actor",
//        "app.bsky.embed",
        "app.bsky.feed"
    )).fold(
        onSuccess = { docs -> docs.forEach { Panorama.codegen(it) }},
        onFailure = { t -> t.printStackTrace() }
    )
}