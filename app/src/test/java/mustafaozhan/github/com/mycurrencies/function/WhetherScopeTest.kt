package mustafaozhan.github.com.mycurrencies.function

import mustafaozhan.github.com.mycurrencies.extensions.ScopeFunctionTest
import org.junit.Assert
import org.junit.Test

class WhetherScopeTest : ScopeFunctionTest() {
    @Test
    fun `whether true`() {
        subject
            ?.whether { it.trueCondition }
            ?.whether { trueCondition }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subject
            ?.whether({ it.trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether  false`() {
        subject
            ?.whether { it.falseCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whether({ it.falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whether mix`() {
        subject
            ?.whether { it.trueCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whether({ it.trueCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot false`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { falseCondition }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subject
            ?.whetherNot({ it.falseCondition }, { falseCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whetherNot true`() {
        subject
            ?.whetherNot { it.trueCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whetherNot({ it.trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot mix`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whetherNot({ it.falseCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }
}
