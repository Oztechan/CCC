package mustafaozhan.github.com.mycurrencies.function.scope

import org.junit.Assert
import org.junit.Test

class EitherScopeTest : MainScopeTest() {

    @Test
    fun `either mix mix`() {
        subjectFunction
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ trueCondition }, { falseCondition })
            ?.let { Assert.assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }

        subjectFunction
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { trueCondition })
            ?.let { Assert.assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }
    }

    @Test
    fun `either mix true`() {
        subjectFunction
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }

        subjectFunction
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { Assert.assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }
    }

    @Test
    fun `either mix false`() {
        subjectFunction
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }

        subjectFunction
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix`() {
        subjectFunction
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { falseCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }

        subjectFunction
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { trueCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix true`() {
        subjectFunction
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }

        subjectFunction
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix false`() {
        subjectFunction
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }

        subjectFunction
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { Assert.assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `eitherNot false false`() {
        subjectFunction
            ?.eitherNot({ it.falseCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }
    }
}
