package mustafaozhan.github.com.mycurrencies.function.lambda

import mustafaozhan.github.com.mycurrencies.function.MainFunctionTest
import org.junit.Assert
import org.junit.Test

class OptionalLambdaTest : MainFunctionTest() {

    private var nullString: String? = null
    private var notNullString: String? = SOME_STRING

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
    fun justInCase() {
        nullString.justInCase {
            nullString = SOME_STRING
        }.apply {
            Assert.assertNotNull(nullString)
            Assert.assertEquals(SOME_STRING, nullString)
        }

        resetString()

        nullString.justInCase {
            nullString = SOME_STRING
        }.let {
            Assert.assertNotNull(nullString)
            Assert.assertEquals(SOME_STRING, nullString)
        }

        resetString()

        nullString.justInCase {
            // not initialized
        }.apply {
            Assert.assertNull(nullString)
        }

        resetString()

        notNullString.justInCase {
            Assert.fail(UN_EXPECTED)
        }.let {
            Assert.assertEquals(SOME_STRING, notNullString)
        }
    }

    private fun resetString() {
        nullString = null
        notNullString = SOME_STRING
    }
}
