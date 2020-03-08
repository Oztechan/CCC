package mustafaozhan.github.com.mycurrencies.function.scope

import mustafaozhan.github.com.mycurrencies.function.MainFunctionTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

open class MainScopeTest : MainFunctionTest() {

    @Test
    fun `is chain breaks`() {

        subjectFunction
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.inCase(true) { assertTrue(EXPECTED, true) }
            ?.inCaseNot(true) { Assert.fail(UN_EXPECTED) }
            ?.whetherNot { it.trueCondition } // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subjectFunction
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.inCase(true) { assertTrue(EXPECTED, true) }
            ?.inCaseNot(true) { Assert.fail(UN_EXPECTED) }
            ?.either({ it.falseCondition }, { falseCondition }) // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `is null passed through scope`() {
        subjectFunction = null
        subjectFunction
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.inCase(true) { assertTrue(EXPECTED, true) }
            ?.inCaseNot(true) { Assert.fail(UN_EXPECTED) }
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            .let {
                if (it == null) {
                    assertTrue(EXPECTED, true)
                } else {
                    Assert.fail(UN_EXPECTED)
                }
            }
        subjectFunction = null
        subjectFunction
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.inCase(true) { assertTrue(EXPECTED, true) }
            ?.inCaseNot(true) { Assert.fail(UN_EXPECTED) }
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
    }
}
