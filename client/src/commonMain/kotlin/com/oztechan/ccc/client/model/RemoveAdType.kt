/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.client.model

import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.util.VIDEO_REWARD

enum class RemoveAdType(val data: RemoveAdData) {
    VIDEO(RemoveAdData("Watch Video", "$VIDEO_REWARD Days", "")),
    MONTH(RemoveAdData("", "1 Month", "one_month")),
    QUARTER(RemoveAdData("", "3 Months", "three_months")),
    HALF_YEAR(RemoveAdData("", "6 Months", "six_months")),
    YEAR(RemoveAdData("", "1 Year", "one_year")),
    LIFE_TIME(RemoveAdData("", "Life time", "life_time"));

    companion object {
        fun getById(sku: String?) = values()
            .whetherNot { sku == null }
            ?.firstOrNull { it.data.id == sku }

        fun getPurchaseIds() = values()
            .map { it.data.id }
            .filter { it.isNotEmpty() }
    }
}
