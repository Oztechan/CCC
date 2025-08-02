package com.oztechan.ccc.client.viewmodel.premium.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PremiumTypeTest {

    @Test
    fun getById() {
        val adType = PremiumType.getById(null)
        assertNull(adType)

        PremiumType.entries.forEach { premiumType ->
            assertEquals(
                premiumType,
                PremiumType.getById(premiumType.data.id)
            )
        }
    }
}
