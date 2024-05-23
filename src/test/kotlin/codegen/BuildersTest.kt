package codegen

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
}