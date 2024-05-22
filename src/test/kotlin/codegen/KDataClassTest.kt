package codegen

import kotlin.test.Test
import kotlin.test.assertEquals

class KDataClassTest {

    @Test
    fun `gen output kdoc is present if not null`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    name = "testing"
                )
            ),
            description = "this is a test",
            name = "Foobar"
        )
        assertEquals(
            """
/**
 * this is a test
 */
@kotlinx.serialization.Serializable
public data class Foobar(
  public val testing: kotlin.String,
)

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `constructor property is nullable`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    isNullable = true,
                    name = "testing"
                )
            ),
            name = "Foobar"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foobar(
  public val testing: kotlin.String?,
)

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `constructor property is not nullable`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    isNullable = false,
                    name = "testing"
                )
            ),
            name = "Foobar"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foobar(
  public val testing: kotlin.String,
)

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `constructor property with default value`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    defaultValue = "bar",
                    name = "testing"
                )
            ),
            name = "Foo"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foo(
  public val testing: kotlin.String = "bar",
)

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `constructor property without default value`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    defaultValue = null,
                    name = "testing"
                )
            ),
            name = "Foo"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foo(
  public val testing: kotlin.String,
)

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `constructor property with validator`() {
        val config = KDataClassConfig(
            constructorProperties = listOf(
                KPropConfig(
                    cls = Int::class,
                    isNullable = false,
                    name = "count",
                    validators = listOf("require(count >= 10)")
                )
            ),
            name = "Foobar"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foobar(
  public val count: kotlin.Int,
) {
  init {
    require(count >= 10)
  }
}

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `body property is nullable`() {
        val config = KDataClassConfig(
            bodyProperties = listOf(
                KPropConfig(
                    cls = Boolean::class,
                    isNullable = true,
                    name = "isOk",
                    constantValue = true
                )
            ),
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    defaultValue = "bar",
                    name = "testing"
                )
            ),
            name = "Foo"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foo(
  public val testing: kotlin.String = "bar",
) {
  public val isOk: kotlin.Boolean? = true
}

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `body property is not nullable`() {
        val config = KDataClassConfig(
            bodyProperties = listOf(
                KPropConfig(
                    cls = Boolean::class,
                    isNullable = false,
                    name = "isOk",
                    constantValue = true
                )
            ),
            constructorProperties = listOf(
                KPropConfig(
                    cls = String::class,
                    defaultValue = "bar",
                    name = "testing"
                )
            ),
            name = "Foo"
        )
        assertEquals(
            """
@kotlinx.serialization.Serializable
public data class Foo(
  public val testing: kotlin.String = "bar",
) {
  public val isOk: kotlin.Boolean = true
}

            """.trimIndent(),
            generateKDataClass(config).toString())
    }

    @Test
    fun `formatterFor string class`() {
        assertEquals("%S", formatterFor(String::class))
    }

    @Test
    fun `formatterFor non-string class`() {
        assertEquals("%L", formatterFor(Int::class))
    }

    @Test
    fun `typeNameFor list class, nullable`() {
        val res = typeNameFor(List::class, Boolean::class, isNullable = true)
        assert(res.toString() == "kotlin.collections.List<kotlin.Boolean?>")
    }

    @Test
    fun `typeNameFor list class, not nullable`() {
        val res = typeNameFor(List::class, Boolean::class, isNullable = false)
        assert(res.toString() == "kotlin.collections.List<kotlin.Boolean>")
    }

    @Test
    fun `typeNameFor non-list class, nullable`() {
        val res = typeNameFor(Boolean::class, null, isNullable = true)
        assert(res.toString() == "kotlin.Boolean?")
    }

    @Test
    fun `typeNameFor non-list class, not nullable`() {
        val res = typeNameFor(Boolean::class, null, isNullable = false)
        assert(res.toString() == "kotlin.Boolean")
    }
}