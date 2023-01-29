package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.client.core.shared.util.nowAsDateString
import com.oztechan.ccc.common.core.model.ExchangeRate

internal fun ExchangeRate.toConversion() = conversion.copy(base = base, date = nowAsDateString())

internal fun ExchangeRate.toTodayResponse() = copy(date = nowAsDateString())
