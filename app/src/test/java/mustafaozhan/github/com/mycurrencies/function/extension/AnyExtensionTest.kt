package mustafaozhan.github.com.mycurrencies.function.extension

import mustafaozhan.github.com.mycurrencies.extension.castTo
import mustafaozhan.github.com.mycurrencies.extension.getThroughReflection
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class AnyExtensionTest {
    companion object {
        const val UN_EXPECTED = "Unexpected"
        const val EXPECTED = "Expected"
    }

    open class A
    open class B : A()

    class C : B() {
        var someInt = 1
        var someString = "Some String"
    }

    @Test
    fun getThroughReflection() {
        val c = C()

        assertEquals(
            c.someString,
            c.getThroughReflection<String>("someString")
        )

        assertEquals(
            c.someInt,
            c.getThroughReflection<Int>("someInt")
        )
    }

    @Test
    fun castTo() {
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
