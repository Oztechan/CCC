package com.oztechan.ccc.client.model

import kotlin.test.Test
import kotlin.test.assertEquals

class RemoveAdTypeTest {

    @Test
    fun getById() {
        val adType = RemoveAdType.getById(null)
        assertEquals(null, adType)

        RemoveAdType.values().forEach { removeAdType ->
            assertEquals(
                removeAdType,
                RemoveAdType.getById(removeAdType.data.id)
            )
        }
    }
}
