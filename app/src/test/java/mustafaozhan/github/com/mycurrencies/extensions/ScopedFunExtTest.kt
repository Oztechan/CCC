package mustafaozhan.github.com.mycurrencies.extensions

import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Test

class ScopedFunExtTest {

    inner class TestClass {
        var trueCondition = true
        var falseCondition = false
    }

    private var subject: TestClass? = TestClass()

    @Test
    fun whether() {
        subject
            ?.whether { it.trueCondition }
            ?.let { assertTrue("Should enter", true) }
            ?: run { Assert.fail("Shouldn't enter") }

        subject
            ?.whether { it.falseCondition }
            ?.let { Assert.fail("Shouldn't enter") }
    }

    @Test
    fun `whether not`() {
        subject
            ?.whetherNot { it.falseCondition }
            ?.let { assertTrue("Should enter", true) }
            ?: run { Assert.fail("Shouldn't enter") }

        subject
            ?.whetherNot { it.trueCondition }
            ?.let { Assert.fail("Shouldn't enter") }
    }

    @Test
    fun `whether this`() {
        subject
            ?.whetherThis { trueCondition }
            ?.apply { assertTrue("Should enter", true) }
            ?: run { Assert.fail("Shouldn't enter") }

        subject
            ?.whetherThis { falseCondition }
            ?.apply { Assert.fail("Shouldn't enter") }
    }

    @Test
    fun `whether this not`() {
        subject
            ?.whetherThisNot { falseCondition }
            ?.apply { assertTrue("Should enter", true) }
            ?: run { Assert.fail("Shouldn't enter") }

        subject
            .whetherThisNot { true }
            ?.apply { Assert.fail("Shouldn't enter") }
    }

    @Test
    fun `chain combination`() {
        subject
            ?.whetherThis { it.trueCondition }
            ?.whetherThisNot { falseCondition }
            ?.whetherNot { it.trueCondition } // exit chain
            ?.whether { true }
            ?.let { Assert.fail("Shouldn't enter") }
            ?: run { assertTrue("Should enter", true) }
    }
}
