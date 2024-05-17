package codegen

import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.KClass

// `const` and `default` cannot both be not null
// TODO does T need a tighter constraint than Any?
data class PropertyConfig<T: Any>(
    val cls: KClass<T>,
    // determines if constructor or class member
    val const: T?,
    // impacts constructor
    val default: T?,
    val itemCls: KClass<out Any>? = null,
    // TODO find a way to not need this kotlinpoet in here
    //val typeName: TypeName,
    // these go in init
    val validators: List<String> = emptyList()
    //val validators: List<Validator<T>> = emptyList()
)