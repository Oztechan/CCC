/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package mustafaozhan.github.com.mycurrencies.extension

import mustafaozhan.github.com.data.model.CurrencyResponse
import mustafaozhan.github.com.data.model.Rates
import mustafaozhan.github.com.data.util.dropDecimal
import mustafaozhan.github.com.data.util.getFormatted
import mustafaozhan.github.com.data.util.getThroughReflection
import mustafaozhan.github.com.data.util.toPercent
import mustafaozhan.github.com.data.util.toRate
import mustafaozhan.github.com.data.util.toStandardDigits
import mustafaozhan.github.com.data.util.toSupportedCharacters
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TypeExtensionTest {

    inner class SubjectModel {
        var someInt = 1
        var someString = "Some String"
    }

    @Test
    fun `replace unsupported characters`() {
        assertEquals(",٫ −".toSupportedCharacters(), "..-")
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
            assertEquals(it.toStandardDigits(), "0123456789")
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
        assertEquals("200/100*5", ("200%5").toPercent())
    }

    @Test
    fun `get through reflection`() {
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

    @Test
    fun `currency response to rate`() {
        val base = "EUR"
        val date = SimpleDateFormat(
            "HH:mm:ss MM.dd.yyyy",
            Locale.ENGLISH
        ).format(Date())

        val rates = Rates(base, date, uSD = 5.0)
        val currencyResponse = CurrencyResponse(base, date, rates)
        assertEquals(
            rates,
            currencyResponse.toRate()
        )
    }
}
