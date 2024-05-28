package codegen

import kotlin.reflect.KClass

sealed interface KtType {
    data class KtDataClass(
        val description: String? = null,
        val members: List<KtMember<*>>,
        val name: String,
    ): KtType

    // data object

    // enum

    data class KtTypeAlias<T: Any>(
        val name: String,
        val type: KClass<T>
    ): KtType

    // value class
}