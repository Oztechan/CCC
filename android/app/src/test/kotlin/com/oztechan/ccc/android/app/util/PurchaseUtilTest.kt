package com.oztechan.ccc.android.app.util

import com.oztechan.ccc.billing.model.ProductDetails
import com.oztechan.ccc.billing.model.PurchaseHistoryRecord
import com.oztechan.ccc.client.model.PremiumType
import com.oztechan.ccc.test.BaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PurchaseUtilTest : BaseTest() {
    @Test
    fun `toPremiumDataList maps correctly`() {
        val productDetailsLists = listOf(
            ProductDetails("1", "asd", "123213"),
            ProductDetails("2", "zxc", "98989")
        )
        val premiumDataList = productDetailsLists.toPremiumDataList()

        productDetailsLists.zip(premiumDataList) { first, second ->
            assertEquals(first.id, second.id)
            assertEquals(first.price, second.cost)
            assertEquals(first.description, second.duration)
        }
    }

    @Test
    fun `toOldPurchaseList maps correctly`() {
        val purchaseHistoryRecordLists = listOf(
            PurchaseHistoryRecord(listOf("1", "2"), 123L),
            PurchaseHistoryRecord(listOf("9", "8"), 987L)
        )

        val oldPurchaseList = purchaseHistoryRecordLists.toOldPurchaseList()

        purchaseHistoryRecordLists.zip(oldPurchaseList) { first, second ->
            assertEquals(first.date, second.date)
            val type = PremiumType.getById(first.ids.firstOrNull())
            assertEquals(type, second.type)
        }
    }
}
