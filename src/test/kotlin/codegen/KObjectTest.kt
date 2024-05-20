package codegen

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KObjectTest {

    @Test
    fun `gen output kdoc is present if not null`() {
        val config = KObjectConfig(
            description = "this is a test",
            name = "foobar",
            properties = listOf(KBodyPropertyConfig(cls = String::class, name = "foo", value = "bar"))
        )
        assertEquals(
            """
/**
 * this is a test
 */
public data object foobar {
  public val foo: kotlin.String = "bar"
}

            """.trimIndent(),
            generateKObject(config).toString())
    }

    @Test
    fun `config properties cannot be empty`() {
        assertFailsWith<IllegalArgumentException> {
            KObjectConfig(
                name = "foobar",
                properties = listOf()
            )
        }
    }

    @Test
    fun `gen output is data object`() {
        val config = KObjectConfig(
            name = "foobar",
            properties = listOf(KBodyPropertyConfig(cls = String::class, name = "foo", value = "bar"))
        )
        assertEquals(
            """
public data object foobar {
  public val foo: kotlin.String = "bar"
}

            """.trimIndent(),
            generateKObject(config).toString())
    }

    @Test
    fun `gen output many properties`() {
        val config = KObjectConfig(
            name = "foobar",
            properties = listOf(
                KBodyPropertyConfig(cls = String::class, name = "foo", value = "bar"),
                KBodyPropertyConfig(cls = Int::class, name = "bar", value = 8)
            )
        )
        assertEquals(
            """
public data object foobar {
  public val foo: kotlin.String = "bar"

  public val bar: kotlin.Int = 8
}

            """.trimIndent(),
            generateKObject(config).toString())
    }
}