package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.helper.BaseTest
import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.core.infrastructure.util.nowAsInstant
import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.model.ExchangeRate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ExchangeRateTest : BaseTest() {

    @Test
    fun toConversion() {
        val base = "EUR"
        val conversion = Conversion(base, nowAsInstant().toDateString(), usd = 5.0)
        val exchangeRate = ExchangeRate(base, nowAsInstant().toDateString(), conversion)
        assertEquals(conversion, exchangeRate.toConversion())
    }

    @Test
    fun toTodayResponse() {
        val exchangeRate = ExchangeRate("EUR", null, Conversion())
        assertEquals(
            exchangeRate.copy(date = nowAsInstant().toDateString()),
            exchangeRate.toTodayResponse()
        )
    }
}
