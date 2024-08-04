package com.oztechan.ccc.android.core.billing

import com.oztechan.ccc.android.core.billing.model.ProductDetails
import com.oztechan.ccc.android.core.billing.model.Purchase

sealed class BillingEffect {
    data object SuccessfulPurchase : BillingEffect()
    data object BillingUnavailable : BillingEffect()

    data class RestorePurchase(
        val purchaseList: List<Purchase>
    ) : BillingEffect()

    data class AddPurchaseMethods(
        val productDetailsList: List<ProductDetails>
    ) : BillingEffect()

    data class UpdatePremiumEndDate(
        val id: String
    ) : BillingEffect()
}
