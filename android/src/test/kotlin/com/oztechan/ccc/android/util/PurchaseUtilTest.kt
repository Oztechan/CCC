package com.oztechan.ccc.android.util

import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod
import com.oztechan.ccc.client.model.RemoveAdType
import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PurchaseUtilTest : BaseTest() {
    @Test
    fun `toRemoveAdDataList maps correctly`() {
        val purchaseMethodList = listOf(
            PurchaseMethod("1", "asd", "123213"),
            PurchaseMethod("2", "zxc", "98989")
        )
        val removeDataList = purchaseMethodList.toRemoveAdDataList()

        purchaseMethodList.zip(removeDataList) { first, second ->
            assertEquals(first.id, second.id)
            assertEquals(first.price, second.cost)
            assertEquals(first.description, second.reward)
        }
    }

    @Test
    fun `toOldPurchaseList maps correctly`() {
        val purchaseHistoryList = listOf(
            PurchaseHistory(listOf("1", "2"), 123L),
            PurchaseHistory(listOf("9", "8"), 987L)
        )

        val oldPurchaseList = purchaseHistoryList.toOldPurchaseList()

        purchaseHistoryList.zip(oldPurchaseList) { first, second ->
            assertEquals(first.date, second.date)
            val type = RemoveAdType.getById(first.ids.firstOrNull())
            assertEquals(type, second.type)
        }
    }
}
