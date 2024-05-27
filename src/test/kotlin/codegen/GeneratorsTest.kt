package codegen

import com.squareup.kotlinpoet.TypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GeneratorsTest {

    @Test
    fun `generateFile contents cannot be empty`() {
        assertFailsWith<IllegalArgumentException> {
            generateFile(
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
            generateFile(
                "test comment",
                listOf(TypeSpec.classBuilder("Baz").build()),
                "foo.bar",
                "Baz"
            ).toString()
        )
    }
}