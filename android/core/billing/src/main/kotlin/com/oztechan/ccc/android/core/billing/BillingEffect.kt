package com.oztechan.ccc.android.core.billing

import com.oztechan.ccc.android.core.billing.model.ProductDetails
import com.oztechan.ccc.android.core.billing.model.PurchaseHistoryRecord

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
