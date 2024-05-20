package codegen

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.Serializable
import java.util.*


internal fun TypeSpec.Companion.dataclass(name: String): TypeSpec.Builder {
    return classBuilder(name)
        .addAnnotation(Serializable::class) // TODO is this needed?
        .addModifiers(KModifier.DATA)
}

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.uppercase(Locale.getDefault()) else it.toString()
    }
}

fun String.uncapitalize(): String {
    return this.replaceFirstChar {
        if (it.isUpperCase()) it.lowercase(Locale.getDefault()) else it.toString()
    }
}