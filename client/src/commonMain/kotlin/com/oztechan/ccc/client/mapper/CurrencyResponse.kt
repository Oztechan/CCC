package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.util.nowAsInstant

internal fun CurrencyResponse.toConversion() = conversion.copy(base = base, date = nowAsInstant().toDateString())

internal fun CurrencyResponse.toTodayResponse() = copy(date = nowAsInstant().toDateString())
