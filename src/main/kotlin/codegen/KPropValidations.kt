package codegen

object KPropValidations {
    fun <T> membership(item: T, collection: List<T>) =
        """require($item in listOf("${collection.joinToString(separator = "\", \"")}"))"""

    fun <T> gte(a: T, b: T) = """require($a >= $b)"""

    fun <T> lte(a: T, b: T) = """require($a <= $b)"""
}