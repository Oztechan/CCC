package com.github.mustafaozhan.ccc.client.mapper

import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyResponseTest {

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
