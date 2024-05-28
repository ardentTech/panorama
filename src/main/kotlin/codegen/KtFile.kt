package codegen

data class KtFile(
    val contents: List<KtType>,
    val description: String,
    val packageName: String,
    val name: String
) {
    init {
        require(contents.isNotEmpty())
    }
}