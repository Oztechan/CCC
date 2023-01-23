package com.oztechan.ccc.common.core.network.mapper

import com.oztechan.ccc.common.core.model.ExchangeRate as ExchangeRateModel
import com.oztechan.ccc.common.core.network.model.ExchangeRate as ExchangeRateAPIModel

fun ExchangeRateAPIModel.toExchangeRateModel(
    fallbackBase: String = base
) = ExchangeRateModel(
    base = fallbackBase,
    date = date,
    conversion = conversion.toConversionModel()
)
