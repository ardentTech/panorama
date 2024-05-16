import codegen.codegen
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import lexicon.*
import java.nio.file.Path
import kotlin.io.path.*

private val logger = KotlinLogging.logger {}

object Panorama {

    private val json by lazy {
        Json {
            classDiscriminator = "discriminator"
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }

    @OptIn(ExperimentalPathApi::class)
    fun codegen(
        destination: Path = Path("./src/main/kotlin"),
        namespaces: List<String>,
        source: Path = Path("./data"),
    ) {
        // TODO should this do `defs.json` and other top-level records, objects and tokens first?
        val docs = source.walk(PathWalkOption.INCLUDE_DIRECTORIES).filter { it.extension == "json" }.map {
            json.decodeFromString<LexiconDoc>(it.readText())
        }.toList()

        namespaces.forEach { namespace ->
            docs.filter { it.id.startsWith(namespace) }.forEach { doc ->
                logger.info { "generating code for ${doc.id} lexicon" }
                doc.codegen(destination)
            }
        }
    }
}

fun main() {
    Panorama.codegen(namespaces = listOf(
        //"com.atproto",
        "app.bsky"
    ))
}