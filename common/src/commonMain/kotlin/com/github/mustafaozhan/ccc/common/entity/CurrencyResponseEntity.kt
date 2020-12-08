/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.common.entity

import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponseEntity(
    @SerialName("base") var base: String,
    @SerialName("date") var date: String? = null,
    @SerialName("rates") var ratesEntity: RatesEntity
)

fun CurrencyResponseEntity.toModel() = CurrencyResponse(
    base, date, ratesEntity.toModel()
)
