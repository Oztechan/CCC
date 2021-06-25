package com.github.mustafaozhan.ccc.common.mapper

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse

internal fun CurrencyResponseEntity.toModel(
    fallbackBase: String = base
) = CurrencyResponse(
    base = fallbackBase,
    date = date,
    rates = rates.toModel()
)
