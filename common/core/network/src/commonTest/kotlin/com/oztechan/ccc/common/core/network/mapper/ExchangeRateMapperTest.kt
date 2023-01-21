package com.oztechan.ccc.common.core.network.mapper

import com.oztechan.ccc.common.core.network.fakes.Fakes
import kotlin.test.Test
import kotlin.test.assertEquals

class ExchangeRateMapperTest {

    @Suppress("LongMethod")
    @Test
    fun toExchangeRateModel() {
        val model = Fakes.exchangeRateAPIModel.toExchangeRateModel()

        assertEquals(Fakes.exchangeRateAPIModel.base, model.base)
        assertEquals(Fakes.exchangeRateAPIModel.date, model.date)
        assertEquals(Fakes.exchangeRateAPIModel.conversion.toConversionModel(), model.conversion)
    }
}
