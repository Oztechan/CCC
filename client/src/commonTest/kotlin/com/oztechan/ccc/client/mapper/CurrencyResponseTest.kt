package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.util.nowAsInstant
import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CurrencyResponseTest : BaseTest() {

    @Test
    fun toConversion() {
        val base = "EUR"
        val conversion = Conversion(base, nowAsInstant().toDateString(), usd = 5.0)
        val currencyResponse = CurrencyResponse(base, nowAsInstant().toDateString(), conversion)
        assertEquals(conversion, currencyResponse.toConversion())
    }

    @Test
    fun toTodayResponse() {
        val currencyResponse = CurrencyResponse("EUR", null, Conversion())
        assertEquals(
            currencyResponse.copy(date = nowAsInstant().toDateString()),
            currencyResponse.toTodayResponse()
        )
    }
}
