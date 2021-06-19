package com.github.mustafaozhan.ccc.client.mapper

import com.github.mustafaozhan.ccc.client.model.Currency as ClientCurrency
import com.github.mustafaozhan.ccc.common.model.Currency as CommonCurrency

fun CommonCurrency.toUIModel() = ClientCurrency(
    name = name,
    longName = longName,
    symbol = symbol,
    rate = rate,
    isActive = isActive
)

fun List<CommonCurrency>.toUIModelList() = map {
    it.toUIModel()
}
