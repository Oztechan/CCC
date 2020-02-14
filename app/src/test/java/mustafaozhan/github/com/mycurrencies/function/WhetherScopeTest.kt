package mustafaozhan.github.com.mycurrencies.function

import mustafaozhan.github.com.mycurrencies.constant.EXPECTED
import mustafaozhan.github.com.mycurrencies.constant.UN_EXPECTED
import org.junit.Assert
import org.junit.Test

class WhetherScopeTest : MainScopeTest() {
    @Test
    fun `whether true`() {
        subjectScope
            ?.whether { it.trueCondition }
            ?.whether { trueCondition }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subjectScope
            ?.whether({ it.trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether  false`() {
        subjectScope
            ?.whether { it.falseCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subjectScope
            ?.whether({ it.falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whether mix`() {
        subjectScope
            ?.whether { it.trueCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subjectScope
            ?.whether({ it.trueCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot false`() {
        subjectScope
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { falseCondition }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subjectScope
            ?.whetherNot({ it.falseCondition }, { falseCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whetherNot true`() {
        subjectScope
            ?.whetherNot { it.trueCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subjectScope
            ?.whetherNot({ it.trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot mix`() {
        subjectScope
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subjectScope
            ?.whetherNot({ it.falseCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }
}
