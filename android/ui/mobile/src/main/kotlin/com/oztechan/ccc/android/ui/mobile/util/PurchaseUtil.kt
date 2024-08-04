package com.oztechan.ccc.android.ui.mobile.util

import com.oztechan.ccc.android.core.billing.model.ProductDetails
import com.oztechan.ccc.android.core.billing.model.Purchase
import com.oztechan.ccc.client.viewmodel.premium.model.OldPurchase
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumData
import com.oztechan.ccc.client.viewmodel.premium.model.PremiumType

internal fun List<ProductDetails>.toPremiumDataList(): List<PremiumData> = map {
    PremiumData(it.price, it.description, it.id)
}

internal fun List<Purchase>.toOldPurchaseList(): List<OldPurchase> =
    mapNotNull { purchase ->
        PremiumType.getById(purchase.products.firstOrNull())?.let {
            OldPurchase(purchase.purchaseTime, it)
        }
    }
