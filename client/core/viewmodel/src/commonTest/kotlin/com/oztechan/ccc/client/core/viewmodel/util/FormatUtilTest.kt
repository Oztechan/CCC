package com.oztechan.ccc.client.core.viewmodel.util

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatUtilTest {
    @Test
    fun toSupportedCharacters() {
        assertEquals("..-", ",٫ −".toSupportedCharacters())
    }

    @Test
    fun getFormatted() {
        val actualDouble1 = 1234567.7891
        assertEquals("1 234 567.789", actualDouble1.getFormatted(3))
        val actualDouble2 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble2.getFormatted(3))
        val actualDouble3 = 1234567.7891
        assertEquals("1 234 567.7891", actualDouble3.getFormatted(4))
        val actualDouble4 = 1234567.7890
        assertEquals("1 234 567.789", actualDouble4.getFormatted(4))
        val actualDouble5 = 0.000000001
        assertEquals("0.000000001", actualDouble5.getFormatted(3))
        val actualDouble6 = 0.0000000001
        assertEquals("0", actualDouble6.getFormatted(3))
    }

    @Test
    fun toStandardDigits() {
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
            assertEquals("0123456789", it.toStandardDigits(), "actual string $it")
        }
    }
}
