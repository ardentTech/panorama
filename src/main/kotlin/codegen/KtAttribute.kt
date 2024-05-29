package codegen

import kotlin.reflect.KClass

fun List<KtAttribute<*>>.parameters() = this.filterIsInstance<KtAttribute.KtParameter<*>>()
fun List<KtAttribute<*>>.properties() = this.filterIsInstance<KtAttribute.KtProperty<*>>()

// TODO figure out how to accommodate ref and union in here...
sealed interface KtAttribute<T: Any> {
    val cls: KClass<T>
    val isNullable: Boolean
    val name: String

    sealed interface KtParameter<T : Any>: KtAttribute<T> {
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

        data class KtReference(
            override val isNullable: Boolean,
            override val name: String,
            val path: String
        ): KtParameter<String> {
            override val cls = String::class
            override val default: String? = null

            val packageName: String
                get() = path.substringBefore("#")

            val typeName: String
                get() = path.substringAfter("#").capitalize()
        }
    }

    sealed interface KtProperty<T : Any>: KtAttribute<T> {
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