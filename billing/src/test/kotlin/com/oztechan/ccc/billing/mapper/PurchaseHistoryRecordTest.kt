package com.oztechan.ccc.billing.mapper

import com.oztechan.ccc.test.BaseTest
import org.junit.Test
import kotlin.test.assertEquals
import com.android.billingclient.api.PurchaseHistoryRecord as PurchaseHistoryRecordIAPModel

class PurchaseHistoryRecordTest : BaseTest() {

    @Test
    fun toPurchaseHistoryRecordModel() {
        val iapModel = PurchaseHistoryRecordIAPModel("mock1", "mock2")
        val model = iapModel.toPurchaseHistoryRecordModel()

        assertEquals(iapModel.products, model.ids)
        assertEquals(iapModel.purchaseTime, model.date)
    }
}
