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

    // fun codegen(source: Path, destination: Path) {}
    // prefixes: ["app.bsky", "com.atproto", "tools.ozone"]
    // rust Vec.push - appends an element to the back of a collection
    // rust Trait std::iter::Extend - extend a collection with contents of an iterator
    // rust quote! macro - turn Rust syntax tree data structures into tokens of source code

    @OptIn(ExperimentalPathApi::class)
    fun codegen(
        destination: Path = Path("./src/main/kotlin/gen"),
        namespaces: List<String>,
        source: String = "./data",
    ) {
        destination.deleteRecursively()
        destination.createDirectory()
        val docs = Path(source).walk(PathWalkOption.INCLUDE_DIRECTORIES).filter { it.extension == "json" }.map { json.decodeFromString<LexiconDoc>(it.readText()) }.toList()

        namespaces.forEach { namespace ->
            docs.filter { it.id.startsWith(namespace) }.forEach { doc -> doc.codegen(destination) }
        }
    }
}

fun main() {
    Panorama.codegen(namespaces = listOf("com.atproto"))
}