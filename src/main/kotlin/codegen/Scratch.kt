package codegen
//
//import kotlin.reflect.KClass
//
//sealed interface KtProperty<out T: Any> {
//    val cls: KClass<out T>
//    val isNullable: Boolean
//    val name: String
//
//    data class KtContainer<out T: Any, out U: Any>(
//        override val cls: KClass<out T>,
//        override val isNullable: Boolean,
//        val itemCls: KClass<out U>,
//        override val name: String
//    ): KtProperty<T>
//
//    data class KtItem<out T: Any>(
//        override val cls: KClass<out T>,
//        val default: T? = null,
//        override val isNullable: Boolean,
//        override val name: String,
//        val value: T? = null,
//    ): KtProperty<T>
//}
