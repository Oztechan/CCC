/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.data.model

import kotlinx.serialization.SerialName

data class CurrencyResponse(
    @SerialName("base") var base: String,
    @SerialName("date") var date: String? = null,
    @SerialName("rates") var rates: Rates
)
