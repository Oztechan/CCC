package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.common.util.nowAsInstant
import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CurrencyResponseTest : BaseTest() {

    @Test
    fun toRates() {
        val base = "EUR"
        val rates = Rates(base, nowAsInstant().toDateString(), usd = 5.0)
        val currencyResponse = CurrencyResponse(base, nowAsInstant().toDateString(), rates)
        assertEquals(rates, currencyResponse.toRates())
    }

    @Test
    fun toTodayResponse() {
        val currencyResponse = CurrencyResponse("EUR", null, Rates())
        assertEquals(
            currencyResponse.copy(date = nowAsInstant().toDateString()),
            currencyResponse.toTodayResponse()
        )
    }
}
