package mustafaozhan.github.com.mycurrencies.extensions

import org.junit.Assert
import org.junit.Assert.assertEquals
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
            ?.whether { trueCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.whether { it.falseCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun whetherNot() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { falseCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.whetherNot { it.trueCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun either() {
        subject
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ true })
            ?.either({ trueCondition }, { falseCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun eitherNot() {
        subject
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { falseCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun mapTo() {
        subject
            ?.mapTo { it.trueCondition }
            ?.whether { it }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.mapTo { it.falseCondition }
            ?.whether { it }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
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
        ?.mapTo { it -> it.length }
        ?.let { assertEquals(11, it) }
        ?: run { Assert.fail(UN_EXPECTED) }
            .mapTo { subject?.trueCondition }
            ?.whether { it }
            ?.mapTo { !it }
            ?.let { Assert.fail(UN_EXPECTED) }
        ?: run { assertTrue(EXPECTED, true) }

    @Test
    fun `is chain breaks`() = subject
        ?.whether { it.trueCondition }
        ?.whetherNot { falseCondition }
        ?.whetherNot { it.trueCondition } // exit chain
        ?.whether { true }
        ?.let { Assert.fail(UN_EXPECTED) }
        ?: run { assertTrue(EXPECTED, true) }

    @Test
    fun `is null passed through scope`() {
        subject = null
        subject
            ?.whether { it.trueCondition }
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
            ?.whetherNot { falseCondition }
            ?.mapTo { it }
            .whether { true }
            ?.let { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun castTo() {
        open class A
        class B : A()

        B().castTo { A::class.java }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        A().castTo { B::class.java }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }
}
