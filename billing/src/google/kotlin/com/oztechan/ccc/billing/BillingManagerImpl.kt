package com.oztechan.ccc.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import co.touchlab.kermit.Logger
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.github.submob.scopemob.whether
import com.oztechan.ccc.billing.model.PurchaseHistory
import com.oztechan.ccc.billing.model.PurchaseMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class BillingManagerImpl(private val context: Context) :
    BillingManager,
    AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchaseHistoryResponseListener,
    ProductDetailsResponseListener {

    private lateinit var billingClient: BillingClient
    private lateinit var scope: CoroutineScope
    private lateinit var productList: List<QueryProductDetailsParams.Product>

    private lateinit var productDetailList: List<ProductDetails>
    private var acknowledgePurchaseParams: AcknowledgePurchaseParams? = null

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun startConnection(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    ) {
        Logger.i { "BillingManagerImpl startConnection" }

        this.scope = lifecycleScope
        this.productList = skuList.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        billingClient = BillingClient
            .newBuilder(context.applicationContext)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(this)
    }

    override fun endConnection() {
        Logger.i { "BillingManagerImpl endConnection" }
        billingClient.endConnection()
    }

    override fun launchBillingFlow(activity: Activity, skuId: String) {
        Logger.i { "BillingManagerImpl launchBillingFlow" }
        productDetailList
            .firstOrNull { it.productId == skuId }
            ?.let {
                val offerToken = it.subscriptionOfferDetails?.get(productDetailList.indexOf(it))?.offerToken.orEmpty()

                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(it)
                        .setOfferToken(offerToken)
                        .build()
                )
                val billingFlowParams =
                    BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()

                billingClient.launchBillingFlow(activity, billingFlowParams)
            }
    }

    override fun acknowledgePurchase() {
        Logger.i { "BillingManagerImpl acknowledgePurchase" }
        acknowledgePurchaseParams?.let {
            billingClient.acknowledgePurchase(it, this)
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        Logger.i { "BillingManagerImpl onAcknowledgePurchaseResponse ${billingResult.responseCode}" }
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            scope.launch {
                _effect.emit(BillingEffect.SuccessfulPurchase)
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        Logger.i { "BillingManagerImpl onPurchasesUpdated ${billingResult.responseCode}" }

        purchaseList?.firstOrNull()
            ?.also {
                acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()
            }
            ?.products
            ?.firstOrNull()
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.UpdateAddFreeDate(it))
                }
            }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        Logger.i { "BillingManagerImpl onBillingSetupFinished ${billingResult.responseCode}" }

        val queryPurchaseHistoryParams = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchaseHistoryAsync(queryPurchaseHistoryParams, this)

        billingClient.whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            queryProductDetailsAsync(queryProductDetailsParams, this@BillingManagerImpl)
        }
    }

    override fun onBillingServiceDisconnected() {
        Logger.i { "BillingManagerImpl onBillingServiceDisconnected" }
    }

    override fun onProductDetailsResponse(
        billingResult: BillingResult,
        productDetasilList: MutableList<ProductDetails>
    ) {
        Logger.i { "BillingManagerImpl onProductDetailsResponse ${billingResult.responseCode}" }

        scope.launch {
            productDetasilList.whether {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            }?.let { detailsList ->
                productDetailList = detailsList

                detailsList
                    .map {
                        PurchaseMethod(
                            price = it.oneTimePurchaseOfferDetails?.formattedPrice.orEmpty(),
                            description = it.description,
                            id = it.productId
                        )
                    }.let {
                        _effect.emit(BillingEffect.AddPurchaseMethods(it))
                    }
            }
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        Logger.i { "BillingManagerImpl onPurchaseHistoryResponse ${billingResult.responseCode}" }

        purchaseHistoryList
            ?.map { PurchaseHistory(it.products, it.purchaseTime) }
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.RestorePurchase(it))
                }
            }
    }
}
