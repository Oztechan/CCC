/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.model

enum class RemoveAdType(val data: RemoveAdData) {
    VIDEO(RemoveAdData("Watch Video", "3 Days", "")),
    MONTH(RemoveAdData("", "1 Month", "one_month")),
    QUARTER(RemoveAdData("", "3 Months", "three_months")),
    HALF_YEAR(RemoveAdData("", "6 Months", "six_months")),
    YEAR(RemoveAdData("", "1 Year", "one_year"))
}

data class RemoveAdData(
    var cost: String,
    var reward: String,
    var skuId: String,
)
