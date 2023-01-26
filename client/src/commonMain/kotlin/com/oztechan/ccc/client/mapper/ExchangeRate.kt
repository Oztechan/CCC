package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.core.shared.util.nowAsInstant
import com.oztechan.ccc.client.core.shared.util.toDateString
import com.oztechan.ccc.common.core.model.ExchangeRate

internal fun ExchangeRate.toConversion() = conversion.copy(base = base, date = nowAsInstant().toDateString())

internal fun ExchangeRate.toTodayResponse() = copy(date = nowAsInstant().toDateString())
