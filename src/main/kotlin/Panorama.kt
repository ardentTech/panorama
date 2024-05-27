import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import lexicon.LexiconDoc
import java.nio.file.Path
import kotlin.io.path.*

private const val JSON_FILE_EXT = "json"
private const val LEXICON_SOURCE_DIR = "./data"
private const val SERIALIZER_CLASS_DISCRIMINATOR = "discriminator"

// TODO test all of this
object Panorama {

    private val json by lazy {
        Json {
            classDiscriminator = SERIALIZER_CLASS_DISCRIMINATOR
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }

    // TODO codegen to stdout
    fun codegen(doc: LexiconDoc): Result<Unit> {
        return Result.success(Unit)
    }

    // TODO doc codegen to filesystem
    fun codegen(doc: LexiconDoc, destination: Path): Result<Unit> {
        return Result.success(Unit)
    }

    // TODO namespace codegen to filesystem
//    fun codegen(source: Path, destination: Path): Result<Unit> {
//        // require that source is a directory
//        // handle `defs.json` first
//        return Result.success(Unit)
//    }

    @OptIn(ExperimentalPathApi::class)
    fun crawl(namespaces: List<String> = emptyList(), source: Path = Path(LEXICON_SOURCE_DIR)): Result<List<LexiconDoc>> =
        try {
            val docs = source.walk(PathWalkOption.INCLUDE_DIRECTORIES).filter { it.extension == JSON_FILE_EXT }.map {
                // TODO logger debug here
                json.decodeFromString<LexiconDoc>(it.readText())
            }.toList()
            Result.success(if (namespaces.isNotEmpty()) { docs.filter { it.namespace in namespaces }} else { docs })
        } catch (e: Exception) {
            Result.failure(e)
        }
}