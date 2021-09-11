package com.github.mustafaozhan.billing

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
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
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.whether
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BillingManager(context: Context) :
    AcknowledgePurchaseResponseListener,
    PurchasesUpdatedListener,
    BillingClientStateListener,
    PurchaseHistoryResponseListener,
    SkuDetailsResponseListener {

    private var billingClient: BillingClient = BillingClient
        .newBuilder(context.applicationContext)
        .enablePendingPurchases()
        .setListener(this)
        .build()
        .also { it.startConnection(this) }

    private lateinit var scope: CoroutineScope
    private lateinit var skuList: List<String>

    private lateinit var skuDetails: List<SkuDetails>
    private var acknowledgePurchaseParams: AcknowledgePurchaseParams? = null

    private val _effect = MutableSharedFlow<BillingEffect>()
    val effect = _effect.asSharedFlow()

    fun setupBillingClient(
        lifecycleScope: LifecycleCoroutineScope,
        skuList: List<String>
    ) {
        kermit.d { "BillingManager setupBillingClient" }

        this.scope = lifecycleScope
        this.skuList = skuList

        scope.launch {
            _effect.emit(BillingEffect.ShowLoading)
        }
    }

    fun endConnection() {
        kermit.d { "BillingManager endConnection" }
        billingClient.endConnection()
    }

    fun launchBillingFlow(activity: Activity, skuId: String) {
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

    fun acknowledgePurchase() {
        acknowledgePurchaseParams?.let {
            billingClient.acknowledgePurchase(it, this)
        } ?: run {
            scope.launch {
                _effect.emit(BillingEffect.RestartActivity)
            }
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        kermit.d { "BillingManager onAcknowledgePurchaseResponse" }
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            scope.launch {
                _effect.emit(BillingEffect.RestartActivity)
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        kermit.d { "BillingManager onPurchasesUpdated ${billingResult.responseCode}" }

        purchaseList?.firstOrNull()
            ?.also {
                acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(it.purchaseToken)
                    .build()
            }
            ?.let {
                scope.launch {
                    _effect.emit(BillingEffect.UpdateAddFreeDate(it.skus.firstOrNull()))
                }
            }
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        kermit.d { "BillingManager onBillingSetupFinished ${billingResult.responseCode}" }

        scope.launch {
            _effect.emit(BillingEffect.HideLoading)
        }

        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, this)

        billingClient.whether(
            { isReady },
            { billingResult.responseCode == BillingClient.BillingResponseCode.OK }
        )?.apply {
            val skuDetailsParams = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.INAPP)
                .build()
            querySkuDetailsAsync(skuDetailsParams, this@BillingManager)
        }
    }

    override fun onBillingServiceDisconnected() {
        kermit.d { "BillingManager onBillingServiceDisconnected" }
        scope.launch {
            _effect.emit(BillingEffect.HideLoading)
        }
    }

    override fun onSkuDetailsResponse(
        billingResult: BillingResult,
        skuDetailsList: MutableList<SkuDetails>?
    ) {
        kermit.d { "BillingManager onSkuDetailsResponse ${billingResult.responseCode}" }

        scope.launch {
            skuDetailsList?.whether {
                billingResult.responseCode == BillingClient.BillingResponseCode.OK
            }?.let { detailsList ->
                skuDetails = detailsList

                _effect.emit(BillingEffect.AddInAppBillingMethods(detailsList))
            } ?: run {
                _effect.emit(BillingEffect.HideLoading)
            }
        }
    }

    override fun onPurchaseHistoryResponse(
        billingResult: BillingResult,
        purchaseHistoryList: MutableList<PurchaseHistoryRecord>?
    ) {
        kermit.d { "BillingManager onPurchaseHistoryResponse ${billingResult.responseCode}" }

        purchaseHistoryList?.let {
            scope.launch {
                _effect.emit(BillingEffect.RestorePurchase(purchaseHistoryList))
            }
        }
    }
}
