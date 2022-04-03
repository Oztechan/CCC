package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.util.nowAsInstant

fun CurrencyResponse.toRates() = rates.copy(base = base, date = nowAsInstant().toDateString())

fun CurrencyResponse.toTodayResponse() = copy(date = nowAsInstant().toDateString())
