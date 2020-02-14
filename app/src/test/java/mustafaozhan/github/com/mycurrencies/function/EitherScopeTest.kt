package mustafaozhan.github.com.mycurrencies.function

import mustafaozhan.github.com.mycurrencies.constant.EXPECTED
import mustafaozhan.github.com.mycurrencies.constant.UN_EXPECTED
import org.junit.Assert
import org.junit.Test

class EitherScopeTest : MainScopeTest() {

    @Test
    fun `either mix mix`() {
        subjectScope
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ trueCondition }, { falseCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subjectScope
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `either mix true`() {
        subjectScope
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subjectScope
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `either mix false`() {
        subjectScope
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        subjectScope
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix`() {
        subjectScope
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        subjectScope
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix true`() {
        subjectScope
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        subjectScope
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix false`() {
        subjectScope
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }

        subjectScope
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot false false`() {
        subjectScope
            ?.eitherNot({ it.falseCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }
}
