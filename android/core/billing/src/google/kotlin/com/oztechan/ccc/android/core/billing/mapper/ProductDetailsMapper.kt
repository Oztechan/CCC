package com.oztechan.ccc.android.core.billing.mapper

import com.android.billingclient.api.ProductDetails as ProductDetailsIAPModel
import com.oztechan.ccc.android.core.billing.model.ProductDetails as ProductDetailsModel

internal fun ProductDetailsIAPModel.toProductDetailsModel() = ProductDetailsModel(
    price = oneTimePurchaseOfferDetails?.formattedPrice.orEmpty(),
    description = description,
    id = productId
)
