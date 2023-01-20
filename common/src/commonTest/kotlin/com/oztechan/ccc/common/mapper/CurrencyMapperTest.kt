package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.database.sql.Currency as CurrencyDBModel

internal class CurrencyMapperTest : BaseTest() {
    @Test
    fun toCurrencyModel() {
        val dbModel = CurrencyDBModel("USD", "United State Dollar", "$", 12.3, 1)

        val model = dbModel.toCurrencyModel()

        assertEquals(dbModel.code, model.code)
        assertEquals(dbModel.name, model.name)
        assertEquals(dbModel.symbol, model.symbol)
        assertEquals(dbModel.rate, model.rate)
        assertEquals(dbModel.isActive.toBoolean(), model.isActive)
    }
}
