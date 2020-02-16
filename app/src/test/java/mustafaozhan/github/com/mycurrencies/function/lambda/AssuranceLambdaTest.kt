package mustafaozhan.github.com.mycurrencies.function.lambda

import mustafaozhan.github.com.mycurrencies.function.MainFunctionTest
import org.junit.Assert
import org.junit.Test

class AssuranceLambdaTest : MainFunctionTest() {

    @Test
    fun assurance() {
        assurance(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition
        ) {
            Assert.assertTrue(EXPECTED, true)
        } ?: run {
            Assert.fail(Companion.UN_EXPECTED)
        }

        assurance(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition,
            subjectFunction?.nullAbleCondition
        ) {
            Assert.fail(UN_EXPECTED)
        } ?: run {
            Assert.assertTrue(Companion.EXPECTED, true)
        }
    }
}
