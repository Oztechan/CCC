/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Suppress("OPT_IN_USAGE")
@Serializable
data class CurrencyResponse(
    @JsonNames("base", "base_code") var base: String,
    @SerialName("date") var date: String? = null,
    @JsonNames("rates", "conversion_rates") var rates: Rates
)
