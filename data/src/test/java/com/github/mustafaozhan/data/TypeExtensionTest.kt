/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data

import com.github.mustafaozhan.data.model.CurrencyResponse
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.data.util.dropDecimal
import com.github.mustafaozhan.data.util.getFormatted
import com.github.mustafaozhan.data.util.getThroughReflection
import com.github.mustafaozhan.data.util.toPercent
import com.github.mustafaozhan.data.util.toRate
import com.github.mustafaozhan.data.util.toStandardDigits
import com.github.mustafaozhan.data.util.toSupportedCharacters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

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
