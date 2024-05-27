package codegen

import com.squareup.kotlinpoet.TypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SourceCodeTest {

    @Test
    fun `formatterFor string`() {
        assertEquals("%S", SourceCode.formatterFor(String::class))
    }

    @Test
    fun `formatterFor not string`() {
        assertEquals("%L", SourceCode.formatterFor(Int::class))
    }

    @Test
    fun `generateDataClass, parameters empty`() {
        assertFailsWith<IllegalArgumentException> {
            SourceCode.generateDataClass(description = "testing", name = "FooBar", parameters = emptyList())
        }
    }

    @Test
    fun `generateDataClass, description null`() {
        assertEquals("""
            |@kotlinx.serialization.Serializable
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |)
            |
        """.trimMargin(),
            SourceCode.generateDataClass(description = null, name = "FooBar", parameters = listOf(
                SourceCode.generateParameter(String::class, default = null, isNullable = false, name = "foo")
            )).toString()
        )
    }

    @Test
    fun `generateDataClass, description not null`() {
        assertEquals("""
            |/**
            | * testing
            | */
            |@kotlinx.serialization.Serializable
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |)
            |
        """.trimMargin(),
            SourceCode.generateDataClass(description = "testing", name = "FooBar", parameters = listOf(
                SourceCode.generateParameter(String::class, default = null, isNullable = false, name = "foo")
            )).toString()
        )
    }

    @Test
    fun `generateDataClass, properties not empty`() {
        assertEquals("""
            |@kotlinx.serialization.Serializable
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |) {
            |  public val bar: kotlin.String = "baz"
            |}
            |
        """.trimMargin(),
            SourceCode.generateDataClass(
                description = null,
                name = "FooBar",
                parameters = listOf(SourceCode.generateParameter(String::class, default = null, isNullable = false, name = "foo")),
                properties = listOf(SourceCode.generateProperty(String::class, "bar", "baz"))
            ).toString()
        )
    }

    @Test
    fun `generateDataObject without description`() {
        assertEquals("""
            |@kotlinx.serialization.Serializable
            |public data object FooBar
            |
            """.trimMargin(),
            SourceCode.generateDataObject(name = "FooBar").toString()
        )
    }

    @Test
    fun `generateDataObject with description`() {
        assertEquals("""
            |/**
            | * foobar
            | */
            |@kotlinx.serialization.Serializable
            |public data object FooBar
            |
            """.trimMargin(),
            SourceCode.generateDataObject(description = "foobar", name = "FooBar").toString()
        )
    }

    @Test
    fun `generateEnum without description`() {
        assertEquals("""
            |public enum class TestEnum {
            |  ONE,
            |  TWO,
            |  THREE,
            |}
            |
            """.trimMargin(),
            SourceCode.generateEnum(listOf("ONE", "TWO", "THREE"), name = "TestEnum").toString()
        )
    }

    @Test
    fun `generateEnum with description`() {
        assertEquals("""
            |/**
            | * foobar
            | */
            |public enum class TestEnum {
            |  ONE,
            |  TWO,
            |  THREE,
            |}
            |
            """.trimMargin(),
            SourceCode.generateEnum(listOf("ONE", "TWO", "THREE"), description = "foobar", name = "TestEnum").toString()
        )
    }

    @Test
    fun `generateFile contents cannot be empty`() {
        assertFailsWith<IllegalArgumentException> {
            SourceCode.generateFile(
                "",
                emptyList(),
                "",
                ""
            )
        }
    }

    @Test
    fun `generateFile format ok`() {
        assertEquals("""
            |// test comment
            |package foo.bar
            |
            |public class Baz
            |
        """.trimMargin(),
            SourceCode.generateFile(
                "test comment",
                listOf(TypeSpec.classBuilder("Baz").build()),
                "foo.bar",
                "Baz"
            ).toString()
        )
    }

    @Test
    fun `generateListParameter, nullable false`() {
        assertEquals("""
            |foo: kotlin.collections.List<kotlin.String>
            """.trimMargin(),
            SourceCode.generateListParameter(isNullable = false, String::class, "foo").toString()
        )
    }

    @Test
    fun `generateListParameter, nullable true`() {
        assertEquals("""
            |foo: kotlin.collections.List<kotlin.Int>?
            """.trimMargin(),
            SourceCode.generateListParameter(isNullable = true, Int::class, "foo").toString()
        )
    }

    @Test
    fun `generateParameter, string, default null, nullable false`() {
        assertEquals("""
            |foo: kotlin.String
            """.trimMargin(),
            SourceCode.generateParameter(String::class, default = null, isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `generateParameter, string, default not null, nullable false`() {
        assertEquals("""
            |foo: kotlin.String = "bar"
            """.trimMargin(),
            SourceCode.generateParameter(String::class, default = "bar", isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `generateParameter, string, default null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String?
            """.trimMargin(),
            SourceCode.generateParameter(String::class, default = null, isNullable = true, name = "foo").toString()
        )
    }

    @Test
    fun `generateParameter, string, default not null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String? = "bar"
            """.trimMargin(),
            SourceCode.generateParameter(String::class, default = "bar", isNullable = true, name = "foo").toString()
        )
    }

    @Test
    fun `generateProperty no value`() {
        assertEquals("""
            |val foo: kotlin.String
            |
            """.trimMargin(),
            SourceCode.generateProperty(String::class, "foo").toString()
        )
    }

    @Test
    fun `generateProperty string value`() {
        assertEquals("""
            |val foo: kotlin.String = "bar"
            |
            """.trimMargin(),
            SourceCode.generateProperty(String::class, "foo", "bar").toString()
        )
    }

    @Test
    fun `generateProperty not string value`() {
        assertEquals("""
            |val foo: kotlin.Int = 8
            |
            """.trimMargin(),
            SourceCode.generateProperty(Int::class, "foo", 8).toString()
        )
    }

    @Test
    fun `generateValueClass format ok`() {
        assertEquals("""
            |@kotlinx.serialization.Serializable
            |public value class FooBar(
            |  public val v: kotlin.String,
            |)
            |
        """.trimMargin(),
            SourceCode.generateValueClass(String::class, "FooBar").toString()
        )
    }
}