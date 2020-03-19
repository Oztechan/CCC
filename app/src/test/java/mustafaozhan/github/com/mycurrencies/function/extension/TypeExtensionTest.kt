package mustafaozhan.github.com.mycurrencies.function.extension

import mustafaozhan.github.com.mycurrencies.extension.dropDecimal
import mustafaozhan.github.com.mycurrencies.extension.getFormatted
import mustafaozhan.github.com.mycurrencies.extension.getThroughReflection
import mustafaozhan.github.com.mycurrencies.extension.replaceNonStandardDigits
import mustafaozhan.github.com.mycurrencies.extension.replaceUnsupportedCharacters
import mustafaozhan.github.com.mycurrencies.extension.toPercent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mariuszgromada.math.mxparser.Expression

class TypeExtensionTest {

    inner class SubjectModel {
        var someInt = 1
        var someString = "Some String"
    }

    @Test
    fun `replace unsupported characters`() {
        assertEquals(",٫ −".replaceUnsupportedCharacters(), "..-")
    }

    @Test
    fun `get formatted string from double`() {
        assertEquals(123456.7890.getFormatted(), "123 456.789")
    }

    @Test
    fun `replace localized numbers with numeric ones`() {
        // https://en.wikipedia.org/w/index.php?title=Hindu%E2%80%93Arabic_numeral_system
        listOf(
            "०१२३४५६७८९",
            "૦૧૨૩૪૫૬૭૮૯",
            "੦੧੨੩੪੫੬੭੮੯",
            "༠༡༢༣༤༥༦༧༨༩",
            "০১২৩৪৫৬৭৮৯",
            "೦೧೨೩೪೫೬೭೮೯",
            "୦୧୨୩୪୫୬୭୮୯",
            "൦൧൨൩൪൫൬൭൮൯",
            "௦௧௨௩௪௫௬௭௮௯",
            "౦౧౨౩౪౫౬౭౮౯",
            "០១២៣៤៥៦៧៨៩",
            "๐๑๒๓๔๕๖๗๘๙",
            "໐໑໒໓໔໕໖໗໘໙",
            "၀၁၂၃၄၅၆၇၈၉",
            "٠١٢٣٤٥٦٧٨٩",
            "۰۱۲۳۴۵۶۷۸۹",
            "۰۱۲۳۴۵۶۷۸۹",
            "᠐᠑᠒᠓᠔᠕᠖᠗᠘᠙"
        ).forEach {
            assertEquals(it.replaceNonStandardDigits(), "0123456789")
        }
    }

    @Test
    fun `drop decimal point from string`() {
        assertEquals("1234.567".dropDecimal(), "1234")
        assertEquals("7 972.932".dropDecimal(), "7972")
        assertEquals("1 2 3434 432.432 .4334".dropDecimal(), "123434432")
    }

    @Test
    fun `to percent`() {
        assertEquals(
            Expression("10+200%5+5-5*3".toPercent()).calculate().toString(),
            "10.0"
        )
    }

    @Test
    fun getThroughReflection() {
        val c = SubjectModel()

        assertEquals(
            c.someString,
            c.getThroughReflection<String>("someString")
        )

        assertEquals(
            c.someInt,
            c.getThroughReflection<Int>("someInt")
        )
    }
}
