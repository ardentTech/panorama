package codegen

import codegen.kotlinpoet.formatterFor
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildersTest {

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
            codegen.kotlinpoet.buildParameter(String::class, default = null, isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default not null, nullable false`() {
        assertEquals("""
            |foo: kotlin.String = "bar"
            """.trimMargin(),
            codegen.kotlinpoet.buildParameter(String::class, default = "bar", isNullable = false, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String?
            """.trimMargin(),
            codegen.kotlinpoet.buildParameter(String::class, default = null, isNullable = true, name = "foo").toString()
        )
    }

    @Test
    fun `buildParameter, string, default not null, nullable true`() {
        assertEquals("""
            |foo: kotlin.String? = "bar"
            """.trimMargin(),
            codegen.kotlinpoet.buildParameter(String::class, default = "bar", isNullable = true, name = "foo").toString()
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