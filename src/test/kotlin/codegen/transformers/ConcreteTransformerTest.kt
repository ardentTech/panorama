package codegen.transformers

import codegen.KtAttribute
import codegen.KtType
import lexicon.*
import kotlin.random.Random
import kotlin.test.Test

class ConcreteTransformerTest {

    @Test
    fun `toAttribute blob`() {
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconBlob(), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == String::class)
            assert(this.default == null)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute boolean, const null`() {
        val default = Random.nextBoolean()
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconBoolean(`const` = null, default = default), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == Boolean::class)
            assert(this.default == default)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute boolean, const not null`() {
        val constant = Random.nextBoolean()
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconBoolean(`const` = constant), isNullable, name)

        assert(res is KtAttribute.KtProperty.KtItem)
        with (res as KtAttribute.KtProperty.KtItem) {
            assert(this.cls == Boolean::class)
            assert(this.constant == constant)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute bytes`() {
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconBytes(), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == String::class)
            assert(this.default == null)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute cid-link`() {
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconCidLink(), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == String::class)
            assert(this.default == null)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute integer, const null`() {
        val default = Random.nextInt(16)
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconInteger(`const` = null, default = default), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == Int::class)
            assert(this.default == default)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute integer, const not null`() {
        val constant = Random.nextInt(16)
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconInteger(`const` = constant), isNullable, name)

        assert(res is KtAttribute.KtProperty.KtItem)
        with (res as KtAttribute.KtProperty.KtItem) {
            assert(this.cls == Int::class)
            assert(this.constant == constant)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute null`() {
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconNull(), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == String::class)
            assert(this.default == null)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toAttribute string, const null`() {
        val default = "bar"
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconString(`const` = null, default = default), isNullable, name)

        assert(res is KtAttribute.KtParameter.KtItem)
        with (res as KtAttribute.KtParameter.KtItem) {
            assert(this.cls == String::class)
            assert(this.default == default)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }


    @Test
    fun `toAttribute string const, not null`() {
        val constant = "bar"
        val isNullable = true
        val name = "foo"
        val res = ConcreteTransformer.toAttribute(LexiconString(`const` = constant), isNullable, name)

        assert(res is KtAttribute.KtProperty.KtItem)
        with (res as KtAttribute.KtProperty.KtItem) {
            assert(this.cls == String::class)
            assert(this.constant == constant)
            assert(this.isNullable == isNullable)
            assert(this.name == name)
        }
    }

    @Test
    fun `toType string`() {
        val name = "foo"
        val default = "bar"
        val res = ConcreteTransformer.toType(LexiconString(default = default), name)

        assert(res is KtType.KtValueClass)
        with (res as KtType.KtValueClass) {
            assert(this.name == name)
            assert(this.parameter.default == default)
        }
    }
}