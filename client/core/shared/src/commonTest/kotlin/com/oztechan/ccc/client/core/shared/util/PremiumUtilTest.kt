package com.oztechan.ccc.client.core.shared.util

import com.oztechan.ccc.client.core.shared.model.PremiumType
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals

class PremiumUtilTest {

    @Test
    fun calculatePremiumEnd() = nowAsLong().let {
        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(days = REWARDED_AD_PREMIUM_IN_DAYS),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.VIDEO.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.MONTH.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 3),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.QUARTER.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(months = 6),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.HALF_YEAR.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 1),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.YEAR.calculatePremiumEnd(it).toInstant()
        )

        assertEquals(
            it.toInstant().plus(
                DateTimePeriod(years = 100),
                TimeZone.currentSystemDefault()
            ),
            PremiumType.LIFE_TIME.calculatePremiumEnd(it).toInstant()
        )
    }
}
