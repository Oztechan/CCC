package com.oztechan.ccc.android.core.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
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
import com.oztechan.ccc.android.core.billing.mapper.toProductDetailsModel
import com.oztechan.ccc.android.core.billing.mapper.toPurchaseHistoryRecordModel
import com.oztechan.ccc.android.core.billing.util.launchWithLifeCycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// Billing will not work on debug builds
// .debug suffix needs to be removed in app-level build.gradle and google-services.json
internal class BillingManagerImpl(private val context: Context) :
    BillingManager,
    AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchaseHistoryResponseListener,
    ProductDetailsResponseListener {

    private lateinit var billingClient: BillingClient
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var productList: List<QueryProductDetailsParams.Product>

    private lateinit var productDetailList: List<ProductDetails>
    private var acknowledgePurchaseParams: AcknowledgePurchaseParams? = null

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun startConnection(
        lifecycleOwner: LifecycleOwner,
        skuList: List<String>
    ) {
        Logger.v { "BillingManagerImpl startConnection" }

        this.lifecycleOwner = lifecycleOwner
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
        Logger.v { "BillingManagerImpl endConnection" }
        billingClient.endConnection()
    }

    override fun launchBillingFlow(activity: Activity, skuId: String) {
        Logger.v { "BillingManagerImpl launchBillingFlow" }
        productDetailList
            .firstOrNull { it.productId == skuId }
            ?.let {
                val offerToken =
                    it.subscriptionOfferDetails?.get(productDetailList.indexOf(it))?.offerToken.orEmpty()

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
        Logger.v { "BillingManagerImpl acknowledgePurchase" }
        acknowledgePurchaseParams?.let {
            billingClient.acknowledgePurchase(it, this)
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        Logger.v { "BillingManagerImpl onAcknowledgePurchaseResponse ${billingResult.responseCode}" }
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            lifecycleOwner.launchWithLifeCycle {
                _effect.emit(BillingEffect.SuccessfulPurchase)
            }
        } else {
            lifecycleOwner.launchWithLifeCycle {
                _effect.emit(BillingEffect.BillingUnavailable)
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        Logger.v { "BillingManagerImpl onPurchasesUpdated ${billingResult.responseCode}" }

        purchaseList?.firstOrNull()
            ?.also {
                acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()
            }
            ?.products
            ?.firstOrNull()
            ?.let {
                lifecycleOwner.launchWithLifeCycle {
                    _effect.emit(BillingEffect.UpdatePremiumEndDate(it))
                }
            } ?: lifecycleOwner.launchWithLifeCycle {
            _effect.emit(BillingEffect.BillingUnavailable)
        }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        Logger.v { "BillingManagerImpl onBillingSetupFinished ${billingResult.responseCode}" }

        val queryPurchaseHistoryParams = QueryPurchaseHistoryParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchaseHistoryAsync(queryPurchaseHistoryParams, this)

        billingClient.whether(
            { it.isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            queryProductDetailsAsync(queryProductDetailsParams, this@BillingManagerImpl)
        } ?: lifecycleOwner.launchWithLifeCycle {
            _effect.emit(BillingEffect.BillingUnavailable)
        }
    }

    override fun onBillingServiceDisconnected() {
        Logger.v { "BillingManagerImpl onBillingServiceDisconnected" }
        lifecycleOwner.launchWithLifeCycle {
            _effect.emit(BillingEffect.BillingUnavailable)
        }
    }

    override fun onProductDetailsResponse(
        billingResult: BillingResult,
        productDetasilList: MutableList<ProductDetails>
    ) {
        Logger.v { "BillingManagerImpl onProductDetailsResponse ${billingResult.responseCode}" }

        lifecycleOwner.launchWithLifeCycle {
            productDetasilList.whether {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            }.let { detailsList ->
                productDetailList = detailsList.orEmpty()

                detailsList
                    ?.map { it.toProductDetailsModel() }
                    .let {
                        _effect.emit(BillingEffect.AddPurchaseMethods(it.orEmpty()))
                    }
            }
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        Logger.v { "BillingManagerImpl onPurchaseHistoryResponse ${billingResult.responseCode}" }

        purchaseHistoryList
            ?.map { it.toPurchaseHistoryRecordModel() }
            ?.let {
                lifecycleOwner.launchWithLifeCycle {
                    _effect.emit(BillingEffect.RestorePurchase(it))
                }
            }
    }
}
