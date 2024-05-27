import kotlin.io.path.*

fun main() {
    Panorama.crawl(
        namespaces = listOf("com.atproto.admin"),
        source = Path("./data/com")
    ).fold(
        onSuccess = { docs -> docs.forEach { Panorama.codegen(it, Path("./src/main/kotlin"))} },
        onFailure = { it.printStackTrace() }
    )
}