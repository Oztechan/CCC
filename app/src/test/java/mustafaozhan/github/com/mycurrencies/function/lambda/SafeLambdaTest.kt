package mustafaozhan.github.com.mycurrencies.function.lambda

import mustafaozhan.github.com.mycurrencies.function.MainFunctionTest
import org.junit.Assert
import org.junit.Test

class SafeLambdaTest : MainFunctionTest() {

    @Test
    fun isAllSafe() {
        ifAllSafe(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition
        ) {
            Assert.assertTrue(Companion.EXPECTED, true)
        } ?: run {
            Assert.fail(Companion.UN_EXPECTED)
        }

        ifAllSafe(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition,
            subjectFunction?.nullAbleCondition
        ) {
            Assert.fail(Companion.UN_EXPECTED)
        } ?: run {
            Assert.assertTrue(Companion.EXPECTED, true)
        }
    }
}
