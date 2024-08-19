package com.oztechan.ccc.android.ui.mobile.util

import com.oztechan.ccc.android.core.billing.model.ProductDetails
import com.oztechan.ccc.android.core.billing.model.Purchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PurchaseUtilTest {
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
        val purchaseLists = listOf(
            Purchase(listOf("1", "2"), 123L, "token1"),
            Purchase(listOf("9", "8"), 987L, "token2")
        )

        val oldPurchaseList = purchaseLists.toOldPurchaseList()

        purchaseLists.zip(oldPurchaseList) { first, second ->
            assertEquals(first.purchaseTime, second.date)
            val type = PremiumType.getById(first.products.firstOrNull())
            assertEquals(type, second.type)
            assertEquals(first.purchaseToken, second.purchaseToken)
        }
    }
}
