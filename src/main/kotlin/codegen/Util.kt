package codegen

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.Serializable
import java.util.*


// TODO create abstraction over typespec details with own API
internal fun TypeSpec.Companion.dataclass(name: String): TypeSpec.Builder {
    return classBuilder(name)
        .addAnnotation(Serializable::class) // TODO is this needed?
        .addModifiers(KModifier.DATA)
}

typealias Validator<T> = (T) -> Boolean

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}