package com.oztechan.ccc.common.core.database.mapper

import com.oztechan.ccc.common.core.database.fakes.Fakes
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CurrencyMapperTest {
    @Test
    fun toCurrencyModel() {
        val model = Fakes.currencyDBModel.toCurrencyModel()

        assertEquals(Fakes.currencyDBModel.code, model.code)
        assertEquals(Fakes.currencyDBModel.name, model.name)
        assertEquals(Fakes.currencyDBModel.symbol, model.symbol)
        assertEquals(Fakes.currencyDBModel.rate, model.rate)
        assertEquals(Fakes.currencyDBModel.isActive.toBoolean(), model.isActive)
    }
}
