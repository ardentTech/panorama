package codegen

import kotlin.test.Test
import kotlin.test.assertEquals

class BuildersTest {

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