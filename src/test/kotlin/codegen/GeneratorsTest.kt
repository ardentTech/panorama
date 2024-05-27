package codegen

import com.squareup.kotlinpoet.TypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GeneratorsTest {

    @Test
    fun `formatterFor string`() {
        assertEquals("%S", SourceCode.formatterFor(String::class))
    }

    @Test
    fun `formatterFor not string`() {
        assertEquals("%L", SourceCode.formatterFor(Int::class))
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
}