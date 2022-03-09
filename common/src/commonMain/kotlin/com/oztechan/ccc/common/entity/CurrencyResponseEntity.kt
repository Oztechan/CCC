/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Suppress("EXPERIMENTAL_API_USAGE")
@Serializable
internal data class CurrencyResponseEntity(
    @JsonNames("base", "base_code") var base: String,
    @SerialName("date") var date: String? = null,
    @JsonNames("rates", "conversion_rates") var rates: RatesEntity
)
