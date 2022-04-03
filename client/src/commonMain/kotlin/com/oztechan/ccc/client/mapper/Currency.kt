package com.oztechan.ccc.client.mapper

import com.oztechan.ccc.common.model.Currency
import com.oztechan.ccc.client.model.Currency as CurrencyUIModel

fun Currency.toUIModel() = CurrencyUIModel(
    name = name,
    longName = longName,
    symbol = symbol,
    rate = rate,
    isActive = isActive
)

fun List<Currency>.toUIModelList() = map {
    it.toUIModel()
}
