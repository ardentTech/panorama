package codegen

import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.KClass


// TODO rename to "PrimitiveConfig"???
// `const` and `default` cannot both be not null
// TODO is `<T: Any>` OK here?
data class PropertyConfig<T: Any>(
    // determines if constructor or class member
    val const: T?,
    // impacts constructor
    val default: T?,
    // TODO find a way to not need this
    //val type: KClass<T>,
    val typeName: TypeName,
    // these go in init
    val validators: List<Validator<T>> = emptyList()
)