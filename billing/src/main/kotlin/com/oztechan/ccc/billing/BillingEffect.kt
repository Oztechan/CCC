package com.oztechan.ccc.billing

import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod

sealed class BillingEffect {
    object SuccessfulPurchase : BillingEffect()

    data class RestorePurchase(
        val purchaseHistoryRecordList: List<PurchaseHistory>
    ) : BillingEffect()

    data class AddPurchaseMethods(
        val purchaseMethodList: List<PurchaseMethod>
    ) : BillingEffect()

    data class UpdateAddFreeDate(
        val id: String
    ) : BillingEffect()
}
