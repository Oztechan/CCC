package com.oztechan.ccc.client.mapper

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import com.oztechan.ccc.client.util.toDateString

fun CurrencyResponse.toRates() = rates.copy(base = base, date = nowAsInstant().toDateString())

fun CurrencyResponse.toTodayResponse() = copy(date = nowAsInstant().toDateString())
