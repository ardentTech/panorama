package codegen

import java.util.*

internal fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase(Locale.getDefault()) else it.toString()
    }
}