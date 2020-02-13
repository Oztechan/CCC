package mustafaozhan.github.com.mycurrencies.function

import mustafaozhan.github.com.mycurrencies.constant.EXPECTED
import mustafaozhan.github.com.mycurrencies.constant.UN_EXPECTED
import mustafaozhan.github.com.mycurrencies.function.model.ScopeTestSubject
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

open class MainScopeTest {

    protected var subjectScope: ScopeTestSubject? = ScopeTestSubject()

    @Test
    fun `is chain breaks`() {

        subjectScope
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.whetherNot { it.trueCondition } // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subjectScope
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.either({ it.falseCondition }, { falseCondition }) // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `is null passed through scope`() {
        subjectScope = null
        subjectScope
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
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
        subjectScope = null
        subjectScope
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
    }
}
