package mustafaozhan.github.com.mycurrencies.extensions

import mustafaozhan.github.com.mycurrencies.constant.EXPECTED
import mustafaozhan.github.com.mycurrencies.constant.UN_EXPECTED
import org.junit.Assert
import org.junit.Test

class AnyExtTest {
    open class A
    open class B : A()
    class C : B()

    @Test
    private fun castTo() {
        B().castTo<A>()
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        A().castTo<B>()
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `multi castTo`() {
        C().castTo<B>()
            ?.castTo<A>()
            ?.let { Assert.assertTrue(EXPECTED, true) }
            ?: run { Assert.fail(UN_EXPECTED) }

        A().castTo<B>()
            ?.castTo<C>()
            ?.let { Assert.fail(UN_EXPECTED) }
            ?: run { Assert.assertTrue(EXPECTED, true) }
    }

    @Test
    fun `extraordinary castTo`() = C().castTo<A>()
        ?.let { Assert.assertTrue(EXPECTED, true) }
        ?: run { Assert.fail(UN_EXPECTED) }
}
