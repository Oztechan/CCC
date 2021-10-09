package com.github.mustafaozhan.billing

import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.SkuDetails

sealed class BillingEffect {
    object SuccessfulPurchase : BillingEffect()

    data class RestorePurchase(
        val purchaseHistoryRecordList: List<PurchaseHistoryRecord>
    ) : BillingEffect()

    data class AddInAppBillingMethods(
        val skuDetailsList: List<SkuDetails>
    ) : BillingEffect()

    data class UpdateAddFreeDate(
        val id: String
    ) : BillingEffect()
}
