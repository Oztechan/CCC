package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.database.sql.Currency as CurrencyDBModel

internal class CurrencyTest : BaseTest() {
    @Test
    fun toModel() {
        val entity = CurrencyDBModel("USD", "United State Dollar", "$", 12.3, 1)

        val model = entity.toModel()

        assertEquals(entity.code, model.code)
        assertEquals(entity.name, model.name)
        assertEquals(entity.symbol, model.symbol)
        assertEquals(entity.rate, model.rate)
        assertEquals(entity.isActive.toBoolean(), model.isActive)
    }
}
