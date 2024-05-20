package codegen

import kotlin.reflect.KClass

internal interface KPropertyConfig<T: Any> {
    val cls: KClass<T>
    val isNullable: Boolean
    val itemCls: KClass<T>?
    val name: String
}

internal data class KBodyPropertyConfig<T: Any>(
    override val cls: KClass<T>,
    override val isNullable: Boolean = false,
    override val itemCls: KClass<T>? = null,
    override val name: String,
    val value: T?,
): KPropertyConfig<T>

internal data class KConstructorPropertyConfig<T: Any>(
    override val cls: KClass<T>,
    val defaultValue: T? = null,
    override val isNullable: Boolean = false,
    override val itemCls: KClass<T>? = null,
    override val name: String,
    val validators: List<String> = emptyList()
): KPropertyConfig<T>