package codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
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

    private fun KtFile.toFileSpec(): FileSpec {
        // differentiate bc the api to add aliases is different than other types
        val aliases = contents.filterIsInstance<KtType.KtTypeAlias<*>>()
        val nonAliases = contents.filter { it !is KtType.KtTypeAlias<*> }

        val spec = FileSpec.builder(packageName, name)
            .addFileComment(description)
            .addTypes(nonAliases.map {
                when (it) {
                    is KtType.KtDataClass -> it.toTypeSpec()
                    // TODO other types
                    else -> throw IllegalArgumentException("Unsupported type: ${it::class.simpleName}")
                }
            })
        aliases.forEach { spec.addTypeAlias(it.toTypeAliasSpec()) }

        return spec.build()
    }

    private fun KtMember.KtParameter<*>.toParameterSpec(): ParameterSpec {
        return if (this is KtMember.KtParameter.KtCollection<*, *>) {
            val spec = ParameterSpec.builder(name, List::class.asClassName().parameterizedBy(itemCls.asTypeName()).copy(nullable = isNullable))
            default?.let { spec.defaultValue(formatterFor(cls), it) }
            spec.build()
        } else {
            val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
            default?.let { spec.defaultValue(formatterFor(cls), it) }
            spec.build()
        }
    }

    private fun KtMember.KtProperty<*>.toPropertySpec(): PropertySpec {
        return if (this is KtMember.KtProperty.KtCollection<*, *>) {
            val spec = PropertySpec.builder(name, cls)
            constant?.let { spec.initializer(formatterFor(cls), it) }
            spec.build()
        } else {
            val spec = PropertySpec.builder(name, cls)
            constant?.let { spec.initializer(formatterFor(cls), it) }
            spec.build()
        }
    }

    private fun KtType.KtDataClass.toTypeSpec(): TypeSpec {
        require(members.isNotEmpty())
        val spec = TypeSpec.classBuilder(name)
            .addAnnotation(Serializable::class)
            .addModifiers(KModifier.DATA)
        description?.let { spec.addKdoc(it) }
        val constructor = FunSpec.constructorBuilder()
        val parameterSpecs = members.parameters().map { it.toParameterSpec() }

        constructor.addParameters(parameterSpecs)
        spec.addProperties(parameterSpecs.map { it.toProperty() })
        spec.primaryConstructor(constructor.build())
        spec.addProperties(members.properties().map { it.toPropertySpec() })

        // TODO validators

        return spec.build()
    }

    private fun KtType.KtTypeAlias<*>.toTypeAliasSpec() = TypeAliasSpec.builder(name, type).build()
}