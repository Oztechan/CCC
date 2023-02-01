package com.oztechan.ccc.client.core.shared.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PremiumTypeTest {

    @Test
    fun getById() {
        val adType = PremiumType.getById(null)
        assertNull(adType)

        PremiumType.values().forEach { premiumType ->
            assertEquals(
                premiumType,
                PremiumType.getById(premiumType.data.id)
            )
        }
    }
}
