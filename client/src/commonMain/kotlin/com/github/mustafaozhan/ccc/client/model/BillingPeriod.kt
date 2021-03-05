/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.model

enum class BillingPeriod(val skuId: String) {
    MONTH("one_month"),
    QUARTER("three_months"),
    HALF_YEAR("six_months"),
    YEAR("one_year")
}
