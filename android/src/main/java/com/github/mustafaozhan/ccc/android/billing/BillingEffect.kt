package com.github.mustafaozhan.ccc.android.billing

import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType

sealed class BillingEffect {
    object RestartActivity : BillingEffect()
    object ShowLoading : BillingEffect()
    object HideLoading : BillingEffect()
    data class RestorePurchase(val purchaseHistoryList: List<PurchaseHistory>) : BillingEffect()
    data class AddInAppBillingMethods(val removeAdDataList: List<RemoveAdData>) : BillingEffect()
    data class UpdateAddFreeDate(val removeAdType: RemoveAdType) : BillingEffect()
}
