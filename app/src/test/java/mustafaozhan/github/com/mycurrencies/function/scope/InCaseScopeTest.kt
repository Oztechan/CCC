package mustafaozhan.github.com.mycurrencies.function.scope

import org.junit.Assert
import org.junit.Test

class InCaseScopeTest : MainScopeTest() {

    @Test
    fun inCase() {
        subjectFunction
            ?.inCase(false) {
                Assert.fail(UN_EXPECTED)
            }

        subjectFunction
            ?.whether { trueCondition }
            ?.inCase(true) { Assert.assertTrue(EXPECTED, true) }
            ?.inCase(false) { Assert.fail(UN_EXPECTED) }
            ?.inCase(true) { Assert.assertTrue(EXPECTED, true) }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun inCaseNot() {
        subjectFunction
            ?.inCaseNot(true) {
                Assert.fail(UN_EXPECTED)
            }

        subjectFunction
            ?.whether { trueCondition }
            ?.inCaseNot(false) { Assert.assertTrue(EXPECTED, true) }
            ?.inCaseNot(true) { Assert.fail(UN_EXPECTED) }
            ?.inCaseNot(false) { Assert.assertTrue(EXPECTED, true) }
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }
}
