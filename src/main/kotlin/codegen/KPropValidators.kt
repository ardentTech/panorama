package codegen

object KPropValidators {

    fun <T> gte(a: T, b: T) = """require($a >= $b)"""

    fun <T> lte(a: T, b: T) = """require($a <= $b)"""

    inline fun <reified T> membership(item: T, collection: List<T>): String = if (item is String) {
        """require("$item" in listOf("${collection.joinToString(separator = "\", \"")}"))"""
    } else {
        """require($item in listOf(${collection.joinToString(separator = ", ")}))"""
    }
}