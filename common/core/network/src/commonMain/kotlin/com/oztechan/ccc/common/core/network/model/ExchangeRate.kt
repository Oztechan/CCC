/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Suppress("OPT_IN_USAGE")
@Serializable
data class ExchangeRate(
    @JsonNames("base", "base_code") var base: String,
    @SerialName("date") var date: String? = null,

    // backend response
    @SerialName("rates")
    // accepted ones for client
    @JsonNames(
        "rates",
        "conversion_rates",
        "conversion"
    )
    var conversion: Conversion
)
