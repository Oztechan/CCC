/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused", "TooManyFunctions")

package com.github.mustafaozhan.ccc.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.SkuDetails
import com.github.mustafaozhan.ad.loadBannerAd
import com.github.mustafaozhan.ccc.client.model.PurchaseHistory
import com.github.mustafaozhan.ccc.client.model.RateState
import com.github.mustafaozhan.ccc.client.model.RemoveAdData
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.castTo
import java.io.FileNotFoundException
import mustafaozhan.github.com.mycurrencies.R

fun ImageView.setBackgroundByName(
    name: String
) = setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int = try {
    resources.getIdentifier(
        name.lowercase().replace("try", "tryy"),
        "drawable",
        packageName
    )
} catch (e: FileNotFoundException) {
    kermit.w(e) { e.message.toString() }
    R.drawable.transparent
}

fun View.hideKeyboard() = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
    ?.castTo<InputMethodManager>()
    ?.hideSoftInputFromWindow(windowToken, 0)

fun FrameLayout.setAdaptiveBannerAd(adId: String, isExpired: Boolean) = if (isExpired) {
    loadBannerAd(adId = adId)
    visible()
} else {
    isEnabled = false
    gone()
}

fun <T> Fragment.getNavigationResult(
    key: String
) = findNavController()
    .currentBackStackEntry
    ?.savedStateHandle
    ?.getLiveData<T>(key)

// todo here needs to be changed
@SuppressLint("RestrictedApi")
fun <T> Fragment.setNavigationResult(
    destinationId: Int,
    result: T,
    key: String
) = findNavController()
    .backStack
    .firstOrNull { it.destination.id == destinationId }
    ?.savedStateHandle?.set(key, result)

fun View?.visibleIf(visible: Boolean) = if (visible) visible() else gone()

fun View.showLoading(visible: Boolean) = if (visible) {
    visible()
    bringToFront()
} else gone()

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun TextView.dataState(state: RateState) = when (state) {
    is RateState.Online -> {
        text = context.getString(R.string.text_online_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, 0, 0)
        visible()
    }
    is RateState.Cached -> {
        text = context.getString(R.string.text_cached_last_updated, state.lastUpdate)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cached, 0, 0, 0)
        visible()
    }
    is RateState.Offline -> {
        text = if (state.lastUpdate.isNullOrEmpty()) {
            context.getString(R.string.text_offline)
        } else {
            context.getString(R.string.text_offline_last_updated, state.lastUpdate)
        }
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_offine, 0, 0, 0)
        visible()
    }
    RateState.Error -> {
        text = context.getString(R.string.text_no_data)
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0)
        visible()
    }
    RateState.None -> gone()
}

fun List<SkuDetails>.toRemoveAdDataList(): List<RemoveAdData> = map {
    RemoveAdData(it.price, it.description, it.sku)
}

fun List<PurchaseHistoryRecord>.toPurchaseHistoryList(): List<PurchaseHistory> =
    mapNotNull { purchaseHistoryRecord ->
        RemoveAdType.getBySku(purchaseHistoryRecord.skus.firstOrNull())?.let {
            PurchaseHistory(purchaseHistoryRecord.purchaseTime, it)
        }
    }
