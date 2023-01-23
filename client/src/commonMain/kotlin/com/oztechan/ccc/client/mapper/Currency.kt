package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.common.core.model.Currency
import com.oztechan.ccc.client.model.Currency as CurrencyUIModel

internal fun Currency.toUIModel() = CurrencyUIModel(
    code = code,
    name = name,
    symbol = symbol,
    rate = rate.toString(),
    isActive = isActive
)

internal fun List<Currency>.toUIModelList() = map {
    it.toUIModel()
}
