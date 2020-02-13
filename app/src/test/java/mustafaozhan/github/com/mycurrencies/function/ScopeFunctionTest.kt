package mustafaozhan.github.com.mycurrencies.extensions

import mustafaozhan.github.com.mycurrencies.function.either
import mustafaozhan.github.com.mycurrencies.function.mapTo
import mustafaozhan.github.com.mycurrencies.function.whether
import mustafaozhan.github.com.mycurrencies.function.whetherNot
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("MayBeConst")
open class ScopeFunctionTest {

    protected var subject: TestSubject? = TestSubject()

    companion object {
        const val UN_EXPECTED = "Unexpected"
        const val EXPECTED = "Expected"
        val SOME_STRING: String? = "Some String"
    }

    open class A
    open class B : A()
    class C : B()

    class TestSubject {
        var trueCondition = true
        var falseCondition = false
    }

    @Test
    fun `is chain breaks`() {

        subject
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.whetherNot { it.trueCondition } // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subject
            ?.whether { it.trueCondition }
            ?.whetherNot { falseCondition }
            ?.either({ it.falseCondition }, { falseCondition }) // exit chain
            ?.whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `is null passed through scope`() {
        subject = null
        subject
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
        subject = null
        subject
            ?.whether { it.trueCondition }
            ?.either({ it.falseCondition }, { trueCondition })
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
    }
}
