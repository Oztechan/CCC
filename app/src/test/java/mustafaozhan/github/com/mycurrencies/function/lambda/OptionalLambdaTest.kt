package mustafaozhan.github.com.mycurrencies.function.lambda

import mustafaozhan.github.com.mycurrencies.function.MainFunctionTest
import org.junit.Assert
import org.junit.Test

class OptionalLambdaTest : MainFunctionTest() {

    @Test
    fun ensure() {
        ensure(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition
        ) {
            Assert.assertTrue(EXPECTED, true)
        } ?: run {
            Assert.fail(Companion.UN_EXPECTED)
        }

        ensure(
            subjectFunction?.falseCondition,
            subjectFunction?.trueCondition,
            subjectFunction?.nullAbleCondition
        ) {
            Assert.fail(UN_EXPECTED)
        } ?: run {
            Assert.assertTrue(Companion.EXPECTED, true)
        }
    }

    @Test
    fun inCase() {
        var nullString: String? = null
        val notNullString: String? = "Not Null"

        nullString.inCase {
            nullString = "Not null anymore"
        }?.apply {
            Assert.assertNotNull(nullString)
        }

        notNullString.inCase {
            Assert.fail(UN_EXPECTED)
        }.let {
            Assert.assertNotNull(it)
        }
    }
}
