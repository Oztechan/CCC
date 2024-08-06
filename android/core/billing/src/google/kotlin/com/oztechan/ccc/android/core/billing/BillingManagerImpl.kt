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
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.github.submob.scopemob.whether
import com.oztechan.ccc.android.core.billing.mapper.toProductDetailsModel
import com.oztechan.ccc.android.core.billing.mapper.toPurchaseModel
import com.oztechan.ccc.android.core.billing.util.launchWithLifeCycle
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// Billing will not work on debug builds
// .debug suffix needs to be removed in app-level build.gradle and google-services.json
@Suppress("TooManyFunctions")
internal class BillingManagerImpl(private val context: Context) :
    BillingManager,
    AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchasesResponseListener,
    ProductDetailsResponseListener,
    ConsumeResponseListener {

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
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().enablePrepaidPlans()
                    .build()
            )
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
                val productDetailsParamsList = listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(it)
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

    override fun consumePurchase(token: String) {
        Logger.v { "BillingManagerImpl consumePurchase" }
        billingClient.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(token).build(), this)
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

        val queryPurchasesParams = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(queryPurchasesParams, this)

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

    override fun onQueryPurchasesResponse(
        billingResult: BillingResult,
        purchasesResponse: MutableList<Purchase>
    ) {
        Logger.v { "BillingManagerImpl onQueryPurchasesResponse ${billingResult.responseCode}" }
        lifecycleOwner.launchWithLifeCycle {
            purchasesResponse
                .map { it.toPurchaseModel() }
                .let {
                    _effect.emit(BillingEffect.RestorePurchase(it))
                }
        }
    }

    override fun onConsumeResponse(billingResult: BillingResult, token: String) {
        Logger.v { "BillingManagerImpl onConsumeResponse ${billingResult.responseCode}, token:$token" }
    }
}
