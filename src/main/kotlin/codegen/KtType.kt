package codegen

import kotlin.reflect.KClass

sealed interface KtType {
    data class KtDataClass(
        val description: String? = null,
        val name: String,
        val parameters: List<KtParameter<*>>,
        val properties: List<KtProperty<*>> = emptyList()
    ): KtType {
        init {
            require(parameters.isNotEmpty())
        }
    }

    // data object

    // enum

    data class KtTypeAlias<T: Any>(
        val name: String,
        val type: KClass<T>
    ): KtType

    // value class
}