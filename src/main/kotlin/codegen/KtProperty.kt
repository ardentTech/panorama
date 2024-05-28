package codegen

import kotlin.reflect.KClass

data class KtProperty<T: Any>(
    val cls: KClass<T>,
    val default: T? = null,
    val isNullable: Boolean = false,
    //val itemCls: KClass<U>,
    val name: String,
    val value: T? = null
)
