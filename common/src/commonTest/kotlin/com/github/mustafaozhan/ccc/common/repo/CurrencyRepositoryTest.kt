package com.github.mustafaozhan.ccc.common.repo

import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepository
import com.github.mustafaozhan.ccc.common.db.currency.CurrencyRepositoryImpl
import com.github.mustafaozhan.ccc.common.db.sql.CurrencyQueries
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.mock

class CurrencyRepositoryTest {

    @Mock
    private val currencyQueries = mock(classOf<CurrencyQueries>())

    private val repository: CurrencyRepository by lazy {
        CurrencyRepositoryImpl(currencyQueries)
    }
}
