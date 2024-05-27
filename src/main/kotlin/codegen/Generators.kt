package codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass

object SourceCode {
    internal fun <T: Any> formatterFor(cls: KClass<T>): String = if (cls == String::class) "%S" else "%L"

    internal fun generateFile(comment: String, contents: List<TypeSpec>, packageName: String, fileName: String): FileSpec {
        require(contents.isNotEmpty())
        return FileSpec.builder(packageName, fileName)
            .addFileComment(comment)
            .addTypes(contents)
            .build()
    }

    internal fun <T: Any> generateParameter(cls: KClass<T>, default: T? = null, isNullable: Boolean = false, name: String): ParameterSpec {
        val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
        default?.let { spec.defaultValue(formatterFor(cls), it) }
        return spec.build()
    }
}