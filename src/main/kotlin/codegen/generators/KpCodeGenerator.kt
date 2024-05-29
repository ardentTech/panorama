package codegen.generators

import codegen.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import kotlinx.serialization.Serializable
import java.nio.file.Path
import kotlin.reflect.KClass

// contain all the kotlin poet details

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
        // differentiate bc the api to add aliases is different from other types
        val aliases = contents.filterIsInstance<KtType.KtTypeAlias<*>>()
        val nonAliases = contents.filter { it !is KtType.KtTypeAlias<*> }

        val spec = FileSpec.builder(packageName, name)
            .addFileComment(description)
            .addTypes(nonAliases.map {
                when (it) {
                    is KtType.KtDataClass -> it.toTypeSpec()
                    is KtType.KtEnum -> it.toTypeSpec()
                    // data object
                    // value class
                    else -> throw IllegalArgumentException("Unsupported type: ${it::class.simpleName}")
                }
            })
        aliases.forEach { spec.addTypeAlias(it.toTypeAliasSpec()) }

        return spec.build()
    }

    private fun KtAttribute.KtParameter<*>.toParameterSpec(): ParameterSpec {
        return if (this is KtAttribute.KtParameter.KtCollection<*, *>) {
            val spec = ParameterSpec.builder(
                name,
                List::class.asClassName().parameterizedBy(itemCls.asTypeName()).copy(nullable = isNullable)
            )
            default?.let { spec.defaultValue(formatterFor(cls), it) }
            spec.build()
        } else if (this is KtAttribute.KtParameter.KtReference) {
            // #localReference or path.to#globalReference
            // package name and simple name
            val spec = ParameterSpec.builder(name, ClassName(this.packageName, this.typeName).copy(nullable = isNullable))
            spec.build()
        } else {
            val spec = ParameterSpec.builder(name, cls.asTypeName().copy(nullable = isNullable))
            default?.let { spec.defaultValue(formatterFor(cls), it) }
            spec.build()
        }
    }

    private fun KtAttribute.KtProperty<*>.toPropertySpec(): PropertySpec {
        return if (this is KtAttribute.KtProperty.KtCollection<*, *>) {
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
        require(attributes.isNotEmpty())
        val spec = TypeSpec.classBuilder(name)
            .addAnnotation(Serializable::class)
            .addModifiers(KModifier.DATA)
        description?.let { spec.addKdoc(it) }
        val constructor = FunSpec.constructorBuilder()
        val parameterSpecs = attributes.parameters().map { it.toParameterSpec() }

        constructor.addParameters(parameterSpecs)
        spec.addProperties(parameterSpecs.map { it.toProperty() })
        spec.primaryConstructor(constructor.build())
        spec.addProperties(attributes.properties().map { it.toPropertySpec() })

        // TODO validators

        return spec.build()
    }

    private fun KtType.KtEnum.toTypeSpec(): TypeSpec {
        val spec = TypeSpec.enumBuilder(name)
        description?.let { spec.addKdoc(it) }
        constants.forEach { spec.addEnumConstant(it) }
        return spec.build()
    }

    private fun KtType.KtTypeAlias<*>.toTypeAliasSpec() = TypeAliasSpec.builder(name, type).build()
}