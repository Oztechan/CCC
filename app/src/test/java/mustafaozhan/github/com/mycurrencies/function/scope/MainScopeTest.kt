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
            ?.whetherNot { it.trueCondition } // exit chain
            ?.whether { true }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { assertTrue(Companion.EXPECTED, true) }

        subjectFunction
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.either({ it.falseCondition }, { falseCondition }) // exit chain
            ?.whether { true }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `is null passed through scope`() {
        subjectFunction = null
        subjectFunction
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            .let {
                if (it == null) {
                    assertTrue(Companion.EXPECTED, true)
                } else {
                    Assert.fail(Companion.UN_EXPECTED)
                }
            }
        subjectFunction = null
        subjectFunction
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
    }
}
