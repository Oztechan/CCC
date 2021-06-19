package com.github.mustafaozhan.ccc.common.mapper

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponse as CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse as CurrencyResponseModel

fun CurrencyResponseEntity.toModel(fallbackBase: String = base) = CurrencyResponseModel(
    fallbackBase, date, rates.toModel()
)
