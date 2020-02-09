package mustafaozhan.github.com.mycurrencies.extensions

import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("MayBeConst")
class ScopedFunExtTest {

    companion object {
        private const val UN_EXPECTED = "Unexpected"
        private const val EXPECTED = "Expected"
        private val SOME_STRING: String? = "Some String"
    }

    inner class TestSubject {
        var trueCondition = true
        var falseCondition = false
    }

    private var subject: TestSubject? = TestSubject()

    @Test
    fun whether() {
        subject
            ?.whether { it.trueCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.whether { it.falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether not`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.whetherNot { it.trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether this`() {
        subject
            ?.whetherThis { trueCondition }
            ?.apply { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.whetherThis { falseCondition }
            ?.apply { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether this not`() {
        subject
            ?.whetherThisNot { falseCondition }
            ?.apply { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            .whetherThisNot { true }
            ?.apply { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `chain combination`() = subject
        ?.whetherThis { it.trueCondition }
        ?.whetherThisNot { falseCondition }
        ?.whetherNot { it.trueCondition } // exit chain
        ?.whether { true }
        ?.let { Assert.fail(UN_EXPECTED) }
        ?: run { assertTrue(EXPECTED, true) }

    @Test
    fun `is null passed through scope`() {
        subject = null
        subject
            ?.whetherThis { it.trueCondition }
            ?.whetherThisNot { falseCondition }
            .whether { true }
            .let {
                if (it == null) {
                    assertTrue(EXPECTED, true)
                } else {
                    Assert.fail(UN_EXPECTED)
                }
            }
    }

    @Test
    fun mapTo() {
        subject
            ?.mapTo { trueCondition }
            ?.whether { it }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.mapTo { falseCondition }
            ?.whether { it }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `extraordinary mapTo`() = subject
        .mapTo { SOME_STRING }
        ?.let { assertTrue(EXPECTED, true) }
        ?: run { Assert.fail(UN_EXPECTED) }
            .mapTo { subject?.trueCondition }
            ?.whether { it }
            ?.let { assertTrue(EXPECTED, true) }
        ?: run { Assert.fail(UN_EXPECTED) }
}
