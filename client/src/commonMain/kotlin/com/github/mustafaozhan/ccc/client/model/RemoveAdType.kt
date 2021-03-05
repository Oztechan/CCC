/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.model

@Suppress("VARIABLE_IN_ENUM")
enum class RemoveAdType(
    var cost: String,
    var reward: String,
    val skuId: String,
) {
    VIDEO("Watch Video", "3 Days", ""),
    MONTH("", "1 Month", "one_month"),
    QUARTER("", "3 Months", "three_months"),
    HALF_YEAR("", "6 Months", "six_months"),
    YEAR("", "1 Year", "one_year")
}
