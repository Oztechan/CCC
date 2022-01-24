package com.github.mustafaozhan.billing

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
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.SkuDetailsResponseListener
import com.github.mustafaozhan.billing.model.PurchaseHistory
import com.github.mustafaozhan.billing.model.PurchaseMethod
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BillingManagerImpl(private val context: Context) :
    BillingManager,
    AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchaseHistoryResponseListener,
    SkuDetailsResponseListener {

    private val billingClient: BillingClient by lazy {
        BillingClient
            .newBuilder(context.applicationContext)
            .setListener(this)
            .enablePendingPurchases()
            .build()
    }
    private lateinit var scope: CoroutineScope
    private lateinit var skuList: List<String>

    private lateinit var skuDetails: List<SkuDetails>
    private var acknowledgePurchaseParams: AcknowledgePurchaseParams? = null

    private val _effect = MutableSharedFlow<BillingEffect>()
    override val effect = _effect.asSharedFlow()

    override fun startConnection(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    ) {
        Logger.i { "BillingManagerImpl startConnection" }

        this.scope = lifecycleScope
        this.skuList = skuList

        billingClient.startConnection(this)
    }

    override fun endConnection() {
        Logger.i { "BillingManagerImpl endConnection" }
        billingClient.endConnection()
    }

    override fun launchBillingFlow(activity: Activity, skuId: String) {
        Logger.i { "BillingManagerImpl launchBillingFlow" }
        skuDetails
            .firstOrNull { it.sku == skuId }
            ?.let {
                val billingFlowParams = BillingFlowParams
                    .newBuilder()
                    .setSkuDetails(it)
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
            ?.skus
            ?.firstOrNull()
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.UpdateAddFreeDate(it))
                }
            }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        Logger.i { "BillingManagerImpl onBillingSetupFinished ${billingResult.responseCode}" }

        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)

        billingClient.whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()
            querySkuDetailsAsync(skuDetailsParams, this@BillingManagerImpl)
        }
    }

    override fun onBillingServiceDisconnected() {
        Logger.i { "BillingManagerImpl onBillingServiceDisconnected" }
    }

    override fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: MutableList<SkuDetails>?
    ) {
        Logger.i { "BillingManagerImpl onSkuDetailsResponse ${billingResult.responseCode}" }

        scope.launch {
            skuDetailsList?.whether {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            }?.let { detailsList ->
                skuDetails = detailsList

                detailsList.map {
                    PurchaseMethod(
                        price = it.price,
                        description = it.description,
                        id = it.sku
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
            ?.map { PurchaseHistory(it.skus, it.purchaseTime) }
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.RestorePurchase(it))
                }
            }
    }
}
