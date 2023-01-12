/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.model

import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.util.REWARDED_AD_PREMIUM_IN_DAYS

enum class PremiumType(val data: PremiumData) {
    VIDEO(PremiumData("Watch Video", "$REWARDED_AD_PREMIUM_IN_DAYS Days", "")),
    MONTH(PremiumData("", "1 Month", "one_month")),
    QUARTER(PremiumData("", "3 Months", "three_months")),
    HALF_YEAR(PremiumData("", "6 Months", "six_months")),
    YEAR(PremiumData("", "1 Year", "one_year")),
    LIFE_TIME(PremiumData("", "Life time", "life_time"));

    companion object {
        fun getById(sku: String?) = values()
            .whetherNot { sku == null }
            ?.firstOrNull { it.data.id == sku }

        fun getPurchaseIds() = values()
            .map { it.data.id }
            .filter { it.isNotEmpty() }
    }
}
