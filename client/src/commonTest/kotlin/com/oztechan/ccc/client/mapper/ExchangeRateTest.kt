package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.client.helper.BaseTest
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.ExchangeRate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ExchangeRateTest : BaseTest() {

    @Test
    fun toConversion() {
        val base = "EUR"
        val conversion = Conversion(base, nowAsDateString(), usd = 5.0)
        val exchangeRate = ExchangeRate(base, nowAsDateString(), conversion)
        assertEquals(conversion, exchangeRate.toConversion())
    }

    @Test
    fun toTodayResponse() {
        val exchangeRate = ExchangeRate("EUR", null, Conversion())
        assertEquals(
            exchangeRate.copy(date = nowAsDateString()),
            exchangeRate.toTodayResponse()
        )
    }
}
