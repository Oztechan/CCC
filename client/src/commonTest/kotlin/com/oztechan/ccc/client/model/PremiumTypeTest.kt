package com.oztechan.ccc.client.model

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class PremiumTypeTest : BaseTest() {

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
