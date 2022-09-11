package com.oztechan.ccc.client.model

import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RemoveAdTypeTest : BaseTest() {

    @Test
    fun getById() {
        val adType = RemoveAdType.getById(null)
        assertNull(adType)

        RemoveAdType.values().forEach { removeAdType ->
            assertEquals(
                removeAdType,
                RemoveAdType.getById(removeAdType.data.id)
            )
        }
    }
}
