package com.github.mustafaozhan.ccc.client.mapper

import com.github.mustafaozhan.ccc.client.util.toDateString
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.util.nowAsInstant

fun CurrencyResponse.toRates() = rates.copy(base = base, date = nowAsInstant().toDateString())

fun CurrencyResponse.toTodayResponse() = copy(date = nowAsInstant().toDateString())
