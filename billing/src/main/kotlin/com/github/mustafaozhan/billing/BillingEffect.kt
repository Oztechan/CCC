package com.github.mustafaozhan.billing

import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.SkuDetails

sealed class BillingEffect {
    object RestartActivity : BillingEffect()
    object ShowLoading : BillingEffect()
    object HideLoading : BillingEffect()
    data class RestorePurchase(val purchaseHistoryList: List<PurchaseHistoryRecord>) :
        BillingEffect()

    data class AddInAppBillingMethods(val skuDetailList: List<SkuDetails>) : BillingEffect()
    data class UpdateAddFreeDate(val sku: String?) : BillingEffect()
}
