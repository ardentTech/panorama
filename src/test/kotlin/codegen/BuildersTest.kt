package codegen

import codegen.kotlinpoet.buildDataClass
import codegen.kotlinpoet.buildParameter
import codegen.kotlinpoet.buildProperty
import codegen.kotlinpoet.formatterFor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BuildersTest {

    @Test
    fun `buildDataClass, parameters empty`() {
        assertFailsWith<IllegalArgumentException> {
            buildDataClass(description = "testing", name = "FooBar", parameters = emptyList())
        }
    }

    @Test
    fun `buildDataClass, description null`() {
        assertEquals("""
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |)
            |
        """.trimMargin(),
            buildDataClass(description = null, name = "FooBar", parameters = listOf(
                buildParameter(String::class, default = null, isNullable = false, name = "foo")
            )).toString()
        )
    }

    @Test
    fun `buildDataClass, description not null`() {
        assertEquals("""
            |/**
            | * testing
            | */
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |)
            |
        """.trimMargin(),
            buildDataClass(description = "testing", name = "FooBar", parameters = listOf(
                buildParameter(String::class, default = null, isNullable = false, name = "foo")
            )).toString()
        )
    }

    @Test
    fun `buildDataClass, properties not empty`() {
        assertEquals("""
            |public data class FooBar(
            |  public val foo: kotlin.String,
            |) {
            |  public val bar: kotlin.String = "baz"
            |}
            |
        """.trimMargin(),
            buildDataClass(
                description = null,
                name = "FooBar",
                parameters = listOf(buildParameter(String::class, default = null, isNullable = false, name = "foo")),
                properties = listOf(buildProperty(String::class, "bar", "baz"))
            ).toString()
        )
    }

    @Test
    fun `buildDataObject without description`() {
        assertEquals("""
            |public data object FooBar
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildDataObject(name = "FooBar").toString()
        )
    }

    @Test
    fun `buildDataObject with description`() {
        assertEquals("""
            |/**
            | * foobar
            | */
            |public data object FooBar
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildDataObject(description = "foobar", name = "FooBar").toString()
        )
    }

    @Test
    fun `buildEnum without description`() {
        assertEquals("""
            |public enum class TestEnum {
            |  ONE,
            |  TWO,
            |  THREE,
            |}
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildEnum(listOf("ONE", "TWO", "THREE"), name = "TestEnum").toString()
        )
    }

    @Test
    fun `buildEnum with description`() {
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
            codegen.kotlinpoet.buildEnum(listOf("ONE", "TWO", "THREE"), description = "foobar", name = "TestEnum").toString()
        )
    }

    @Test
    fun `buildParameter, string, default null, nullable false`() {
        assertEquals("""
            |foo: kotlin.String
            """.trimMargin(),
            buildParameter(String::class, default = null, isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default not null, nullable false`() {
        assertEquals("""
            |foo: kotlin.String = "bar"
            """.trimMargin(),
            buildParameter(String::class, default = "bar", isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String?
            """.trimMargin(),
            buildParameter(String::class, default = null, isNullable = true, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default not null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String? = "bar"
            """.trimMargin(),
            buildParameter(String::class, default = "bar", isNullable = true, name = "foo").toString()
        )
    }

    @Test
    fun `buildListParameter, nullable false`() {
        assertEquals("""
            |foo: kotlin.collections.List<kotlin.String>
            """.trimMargin(),
            codegen.kotlinpoet.buildListParameter(isNullable = false, String::class, "foo").toString()
        )
    }

    @Test
    fun `buildListParameter, nullable true`() {
        assertEquals("""
            |foo: kotlin.collections.List<kotlin.Int>?
            """.trimMargin(),
            codegen.kotlinpoet.buildListParameter(isNullable = true, Int::class, "foo").toString()
        )
    }

    @Test
    fun `buildProperty no value`() {
        assertEquals("""
            |val foo: kotlin.String
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildProperty(String::class, "foo").toString()
        )
    }

    @Test
    fun `buildProperty string value`() {
        assertEquals("""
            |val foo: kotlin.String = "bar"
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildProperty(String::class, "foo", "bar").toString()
        )
    }

    @Test
    fun `buildProperty not string value`() {
        assertEquals("""
            |val foo: kotlin.Int = 8
            |
            """.trimMargin(),
            codegen.kotlinpoet.buildProperty(Int::class, "foo", 8).toString()
        )
    }

    @Test
    fun `formatterFor string`() {
        assertEquals("%S", formatterFor(String::class))
    }

    @Test
    fun `formatterFor not string`() {
        assertEquals("%L", formatterFor(Int::class))
    }
}