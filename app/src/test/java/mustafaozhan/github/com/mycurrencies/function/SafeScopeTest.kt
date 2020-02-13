package mustafaozhan.github.com.mycurrencies.function

import mustafaozhan.github.com.mycurrencies.constant.EXPECTED
import mustafaozhan.github.com.mycurrencies.constant.UN_EXPECTED
import org.junit.Assert
import org.junit.Test

class SafeScopeTest : MainScopeTest() {

    @Test
    fun isAllSafe() {
        ifAllSafe(
            subjectScope?.falseCondition,
            subjectScope?.trueCondition
        ) {
            Assert.assertTrue(EXPECTED, true)
        } ?: run {
            Assert.fail(UN_EXPECTED)
        }

        ifAllSafe(
            subjectScope?.falseCondition,
            subjectScope?.trueCondition,
            subjectScope?.nullAbleCondition
        ) {
            Assert.fail(UN_EXPECTED)
        } ?: run {
            Assert.assertTrue(EXPECTED, true)
        }
    }
}
