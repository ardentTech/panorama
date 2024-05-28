package codegen

import kotlin.reflect.KClass

fun List<KtMember<*>>.parameters() = this.filterIsInstance<KtMember.KtParameter<*>>()
fun List<KtMember<*>>.properties() = this.filterIsInstance<KtMember.KtProperty<*>>()

sealed interface KtMember<T: Any> {
    val cls: KClass<T>
    val isNullable: Boolean
    val name: String

    sealed interface KtParameter<T : Any>: KtMember<T> {
        val default: T?

        data class KtItem<T: Any>(
            override val cls: KClass<T>,
            override val default: T? = null,
            override val isNullable: Boolean,
            override val name: String
        ): KtParameter<T>

        data class KtCollection<T: Any, U: Any>(
            override val cls: KClass<T>,
            override val default: T? = null,
            override val isNullable: Boolean,
            val itemCls: KClass<U>,
            override val name: String
        ): KtParameter<T>
    }

    sealed interface KtProperty<T : Any>: KtMember<T> {
        val constant: T?

        data class KtItem<T: Any>(
            override val cls: KClass<T>,
            override val constant: T?,
            override val isNullable: Boolean,
            override val name: String
        ): KtProperty<T>

        data class KtCollection<T: Any, U: Any>(
            override val cls: KClass<T>,
            override val constant: T?,
            override val isNullable: Boolean,
            val itemCls: KClass<U>,
            override val name: String
        ): KtProperty<T>
    }
}