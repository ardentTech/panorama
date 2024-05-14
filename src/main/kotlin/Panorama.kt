import codegen.codegen
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import lexicon.*
import java.nio.file.Path
import kotlin.io.path.*

object Panorama {

    private val json by lazy {
        Json {
            classDiscriminator = "discriminator"
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }

    @OptIn(ExperimentalPathApi::class)
    fun codegen(
        destination: Path = Path("./src/main/kotlin/gen"),
        namespaces: List<String>,
        source: Path = Path("./data"),
    ) {
        val docs = source.walk(PathWalkOption.INCLUDE_DIRECTORIES).filter { it.extension == "json" }.map {
            json.decodeFromString<LexiconDoc>(it.readText())
        }.toList()

        namespaces.forEach { namespace ->
            docs.filter { it.id.startsWith(namespace) }.forEach { doc ->
                val docDestination = Path(base = destination.toString(), *doc.id.split(".").dropLast(1).toTypedArray())
                docDestination.createDirectories()
                doc.codegen(docDestination)
            }
        }
    }
}

fun main() {
    Panorama.codegen(namespaces = listOf(
        "com.atproto",
        "app.bsky"
    ))
}