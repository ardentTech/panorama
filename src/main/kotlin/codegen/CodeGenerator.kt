package codegen

import com.squareup.kotlinpoet.*
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.reflect.KClass

interface CodeGenerator {
    // if this accepted a LexiconDoc instead of KtFile it would simplify the entrypoint
    fun generate(file: KtFile) // stdout
    // if this accepted a LexiconDoc instead of KtFile it would simplify the entrypoint
    fun generate(destination: Path, file: KtFile) // fs
}

object KpCodeGenerator: CodeGenerator {

    override fun generate(file: KtFile) {
        file.toFileSpec().writeTo(System.out)
    }

    override fun generate(destination: Path, file: KtFile) {
        file.toFileSpec().writeTo(destination)
    }

    private fun <T: Any> formatterFor(cls: KClass<T>): String = if (cls == String::class) "%S" else "%L"

    private fun ParameterSpec.toProperty(): PropertySpec = PropertySpec.builder(name, type).initializer(name).build()

    private fun KtFile.toFileSpec() = FileSpec.builder(packageName, name)
        .addFileComment(description)
        .addTypes(contents.map {
            when (it) {
                is KtType.KtDataClass -> it.toTypeSpec()
                // TODO other types
                else -> throw IllegalArgumentException("TODO")
            }
        })
        .build()

    private fun KtParameter<*>.toParameterSpec(): ParameterSpec {
        val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
        default?.let { spec.defaultValue(formatterFor(cls), it) }
        return spec.build()
    }

    private fun KtProperty<*>.toPropertySpec(): PropertySpec {
        val spec = PropertySpec.builder(name, cls)
        value?.let { spec.initializer(formatterFor(cls), it) }
        return spec.build()
    }

    private fun KtType.KtDataClass.toTypeSpec(): TypeSpec {
        require(parameters.isNotEmpty())
        val spec = TypeSpec.classBuilder(name)
            .addAnnotation(Serializable::class)
            .addModifiers(KModifier.DATA)
        description?.let { spec.addKdoc(it) }
        val constructor = FunSpec.constructorBuilder()
        val parameterSpecs = parameters.map { it.toParameterSpec() }

        constructor.addParameters(parameterSpecs)
        spec.addProperties(parameterSpecs.map { it.toProperty() })
        spec.primaryConstructor(constructor.build())
        spec.addProperties(properties.map { it.toPropertySpec() })

        // TODO validators

        return spec.build()
    }
}