package codegen.transformers

import codegen.KtType
import lexicon.LexiconArray
import lexicon.LexiconInteger
import kotlin.test.Test

class ContainerTransformerTest {

    @Test
    fun `toType array, items blob`() {}

    @Test
    fun `toType array, items boolean`() {}

    @Test
    fun `toType array, items bytes`() {}

    @Test
    fun `toType array, items cid link`() {}

    @Test
    fun `toType array, items int`() {
        val input = LexiconArray(items = LexiconInteger())
        val name = "foo"
        val res = ContainerTransformer.toType(input, name)

        assert(res is KtType.KtCollection<*, *>)
        with (res as KtType.KtCollection<*, *>) {
            assert(this.cls == List::class)
            assert(this.itemCls == Int::class)
            assert(this.name == name)
        }
    }

    @Test
    fun `toType array, items ref`() {}

    @Test
    fun `toType array, items string`() {}

    @Test
    fun `toType array, items union`() {}

    @Test
    fun `toType array, items unknown`() {}

    @Test
    fun `toType object`() {

    }

    @Test
    fun `toType params`() {

    }
}