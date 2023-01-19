package com.oztechan.ccc.billing

import com.oztechan.ccc.billing.model.ProductDetails
import com.oztechan.ccc.billing.model.PurchaseHistoryRecord

sealed class BillingEffect {
    object SuccessfulPurchase : BillingEffect()

    data class RestorePurchase(
        val purchaseHistoryRecordRecordList: List<PurchaseHistoryRecord>
    ) : BillingEffect()

    data class AddPurchaseMethods(
        val productDetailsList: List<ProductDetails>
    ) : BillingEffect()

    data class UpdatePremiumEndDate(
        val id: String
    ) : BillingEffect()
}
