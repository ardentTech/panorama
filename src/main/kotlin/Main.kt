import codegen.converters.convertToFile
import kotlin.io.path.*

fun main() {
    Panorama.crawl(
        namespaces = listOf("com.atproto.admin"),
        source = Path("./data/com")
    ).fold(
        onSuccess = { docs -> docs.forEach { println(it.convertToFile()) } },
        onFailure = { it.printStackTrace() }
    )
}