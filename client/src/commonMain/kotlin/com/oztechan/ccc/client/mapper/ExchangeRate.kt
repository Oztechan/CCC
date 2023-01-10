package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.util.toDateString
import com.oztechan.ccc.common.model.ExchangeRate
import com.oztechan.ccc.common.util.nowAsInstant

internal fun ExchangeRate.toConversion() = conversion.copy(base = base, date = nowAsInstant().toDateString())

internal fun ExchangeRate.toTodayResponse() = copy(date = nowAsInstant().toDateString())
