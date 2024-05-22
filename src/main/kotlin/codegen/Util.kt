package codegen

import java.util.*

fun String.camelToEnumCase(): String {
    val pattern = "(?<=.)[A-Z]".toRegex()
    return this.replace(pattern, "_$0").uppercase(Locale.getDefault())
}

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase(Locale.getDefault()) else it.toString()
    }
}

// TODO unit test
fun String.uncapitalize(): String {
    return this.replaceFirstChar {
        if (it.isUpperCase()) it.lowercase(Locale.getDefault()) else it.toString()
    }
}