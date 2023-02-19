package com.oztechan.ccc.client.datasource.currency.mapper

import com.oztechan.ccc.client.datasource.currency.fakes.Fakes
import com.oztechan.ccc.common.core.database.mapper.toBoolean
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CurrencyMapperTest {
    @Test
    fun toCurrencyModel() {
        val model = Fakes.currencyDBModel.toCurrencyModel()

        assertEquals(Fakes.currencyDBModel.code, model.code)
        assertEquals(Fakes.currencyDBModel.name, model.name)
        assertEquals(Fakes.currencyDBModel.symbol, model.symbol)
        assertEquals(Fakes.currencyDBModel.rate.toString(), model.rate)
        assertEquals(Fakes.currencyDBModel.isActive.toBoolean(), model.isActive)
    }
}
