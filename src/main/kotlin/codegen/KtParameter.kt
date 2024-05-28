package codegen

import kotlin.reflect.KClass

data class KtParameter<T: Any>(
    val cls: KClass<T>,
    val default: T? = null,
    val isNullable: Boolean = false,
    val name: String
) {
    companion object {
        fun <T: Any>from(property: KtProperty<T>) = KtParameter(
            cls = property.cls,
            default = property.default,
            isNullable = property.isNullable,
            name = property.name
        )
    }
}