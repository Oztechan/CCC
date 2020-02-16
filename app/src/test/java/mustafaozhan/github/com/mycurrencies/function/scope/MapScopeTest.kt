package mustafaozhan.github.com.mycurrencies.function.scope

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MapScopeTest : MainScopeTest() {

    @Test
    fun mapTo() {
        subjectFunction
            ?.mapTo { it.trueCondition }
            ?.whether { it }
            ?.let { assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }

        subjectFunction
            ?.mapTo { it.falseCondition }
            ?.whether { it }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { assertTrue(Companion.EXPECTED, true) }
        subjectFunction
            ?.mapTo { trueCondition }
            ?.whether { it }
            ?.let { assertTrue(Companion.EXPECTED, true) }
            ?: run { Assert.fail(Companion.UN_EXPECTED) }

        subjectFunction
            ?.mapTo { falseCondition }
            ?.whether { it }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
            ?: run { assertTrue(Companion.EXPECTED, true) }
    }

    @Test
    fun `extraordinary mapTo`() = subjectFunction
        .mapTo { SOME_STRING }
        ?.mapTo { it -> it.length }
        ?.let { assertEquals(11, it) }
        ?: run { Assert.fail(Companion.UN_EXPECTED) }
            .mapTo { subjectFunction?.trueCondition }
            ?.whether { it }
            ?.mapTo { !it }
            ?.let { Assert.fail(Companion.UN_EXPECTED) }
        ?: run { assertTrue(Companion.EXPECTED, true) }
}
