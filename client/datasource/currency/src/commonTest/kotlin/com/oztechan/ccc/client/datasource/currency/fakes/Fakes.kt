package com.oztechan.ccc.client.datasource.currency.fakes

import com.oztechan.ccc.common.core.database.sql.Currency

object Fakes {
    private const val base = "EUR"
    val currencyDBModel = Currency(base, "United State Dollar", "$", 12.3, 1)
}
