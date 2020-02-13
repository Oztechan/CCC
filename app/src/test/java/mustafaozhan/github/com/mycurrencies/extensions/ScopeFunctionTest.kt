package mustafaozhan.github.com.mycurrencies.extensions

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("MayBeConst", "TooManyFunctions")
class ScopeFunctionTest {

    companion object {
        private const val UN_EXPECTED = "Unexpected"
        private const val EXPECTED = "Expected"
        private val SOME_STRING: String? = "Some String"
    }

    open class A
    open class B : A()
    class C : B()

    inner class TestSubject {
        var trueCondition = true
        var falseCondition = false
    }

    private var subject: TestSubject? = TestSubject()

    @Test
    fun `whether true`() {
        subject
            ?.whether { it.trueCondition }
            ?.whether { trueCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subject
            ?.whether({ it.trueCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whether  false`() {
        subject
            ?.whether { it.falseCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whether({ it.falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whether mix`() {
        subject
            ?.whether { it.trueCondition }
            ?.whether { falseCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whether({ it.trueCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot false`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { falseCondition }
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        // vararg
        subject
            ?.whetherNot({ it.falseCondition }, { falseCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `whetherNot true`() {
        subject
            ?.whetherNot { it.trueCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whetherNot({ it.trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `whetherNot mix`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.whetherNot { trueCondition }
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        // vararg
        subject
            ?.whetherNot({ it.falseCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `either mix mix`() {
        subject
            ?.either({ it.trueCondition }, { it.falseCondition })
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
    fun `either mix true`() {
        subject
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        subject
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ trueCondition }, { trueCondition })
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }
    }

    @Test
    fun `either mix false`() {
        subject
            ?.either({ it.trueCondition }, { it.falseCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subject
            ?.either({ it.falseCondition }, { it.trueCondition })
            ?.either({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix`() {
        subject
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subject
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix true`() {
        subject
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subject
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ trueCondition }, { trueCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot mix false`() {
        subject
            ?.eitherNot({ it.trueCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }

        subject
            ?.eitherNot({ it.falseCondition }, { it.trueCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `eitherNot false false`() {
        subject
            ?.eitherNot({ it.falseCondition }, { it.falseCondition })
            ?.eitherNot({ falseCondition }, { falseCondition })
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

    @Test
    fun castTo() {
        B().castTo<B, A>()
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        A().castTo<A, B>()
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `multi castTo`() {
        C().castTo<C, B>()
            ?.castTo<B, A>()
            ?.let { assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        A().castTo<A, B>()
            ?.castTo<B, C>()
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { assertTrue(EXPECTED, true) }
    }

    @Test
    fun `extraordinary castTo`() = C().castTo<C, A>()
        ?.let { assertTrue(EXPECTED, true) }
        ?: run { Assert.fail(UN_EXPECTED) }
}
