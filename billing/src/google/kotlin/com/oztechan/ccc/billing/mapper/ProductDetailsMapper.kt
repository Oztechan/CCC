package com.oztechan.ccc.billing.mapper

import com.android.billingclient.api.ProductDetails as ProductDetailsIAPModel
import com.oztechan.ccc.billing.model.ProductDetails as ProductDetailsModel

internal fun ProductDetailsIAPModel.toProductDetailsModel() = ProductDetailsModel(
    price = oneTimePurchaseOfferDetails?.formattedPrice.orEmpty(),
    description = description,
    id = productId
)
