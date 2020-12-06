/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("unused", "TooManyFunctions")

package com.github.mustafaozhan.ccc.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mustafaozhan.ccc.common.kermit
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.OfflineRates
import com.github.mustafaozhan.scopemob.castTo
import com.github.mustafaozhan.scopemob.mapTo
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.io.FileNotFoundException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Clock
import mustafaozhan.github.com.mycurrencies.R

private const val DATE_FORMAT = "HH:mm dd.MM.yyyy"
private const val MAXIMUM_FLOATING_POINT = 15

fun ImageView.setBackgroundByName(name: String) =
    setImageResource(context.getImageResourceByName(name))

fun Context.getImageResourceByName(name: String): Int = try {
    resources.getIdentifier(
        name.toLowerCase(Locale.getDefault()).replace("try", "tryy"),
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
    ?.toUnit()

fun FrameLayout.setAdaptiveBannerAd(adId: String, isExpired: Boolean) = if (isExpired) {
    MobileAds.initialize(context)
    with(context.resources.displayMetrics) {

        var adWidthPixels = width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = widthPixels.toFloat()
        }
        addView(
            AdView(context).apply {
                adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    (adWidthPixels / density).toInt()
                )
                adUnitId = adId
                loadAd(AdRequest.Builder().build())
            }
        )
    }
} else {
    isEnabled = false
    visibility = View.GONE
}

fun <T> Fragment.getNavigationResult(key: String) =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

// todo here needs to be changed
@SuppressLint("RestrictedApi")
fun <T> Fragment.setNavigationResult(destinationId: Int, result: T, key: String) =
    findNavController()
        .backStack
        .firstOrNull { it.destination.id == destinationId }
        ?.savedStateHandle?.set(key, result)

fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

@Suppress("unused")
fun Any?.toUnit() = Unit

fun Long.isWeekPassed(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= WEEK
}

fun Long.isRewardExpired(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= AD_EXPIRATION
}

fun Rates?.calculateResult(name: String, value: String?) =
    this?.whetherNot { value.isNullOrEmpty() }
        ?.getThroughReflection<Double>(name)
        ?.times(value?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
        ?: 0.0

fun Currency.getCurrencyConversionByRate(base: String, rate: Rates?) =
    "1 $base = " + "${rate?.getThroughReflection<Double>(name)} ${getVariablesOneLine()}"

fun MutableList<Currency>?.removeUnUsedCurrencies(): MutableList<Currency>? =
    this?.filterNot { (name) ->
        name == CurrencyType.BYR.toString() ||
                name == CurrencyType.LVL.toString() ||
                name == CurrencyType.LTL.toString() ||
                name == CurrencyType.ZMK.toString() ||
                name == CurrencyType.CRYPTO_BTC.toString()
    }?.toMutableList()

fun MutableList<Currency>?.toValidList(currentBase: String) =
    this?.filter {
        it.name != currentBase &&
                it.isActive &&
                it.rate.toString() != "NaN" &&
                it.rate.toString() != "0.0"
    } ?: mutableListOf()

fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.whether { Character.isDigit(it) }
            ?.whetherNot { it in '0'..'9' }
            ?.mapTo { Character.getNumericValue(it) }
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
    }
    return builder.toString()
}

fun String.toSupportedCharacters() =
    replace(",", ".")
        .replace("٫", ".")
        .replace(" ", "")
        .replace("−", "-")

fun String.toPercent() = replace("%", "/100*")

fun Double.getFormatted(): String {

    var decimalFormat = "###,###.###"
    val symbols = DecimalFormatSymbols.getInstance()
    symbols.groupingSeparator = ' '

    // increasing floating digits for too small numbers
    for (i in 0..MAXIMUM_FLOATING_POINT) {
        if (DecimalFormat(decimalFormat, symbols).format(this) == "0") {
            decimalFormat = "$decimalFormat#"
        }
    }
    decimalFormat = "$decimalFormat#"
    return DecimalFormat(decimalFormat, symbols).format(this)
}

fun String.dropDecimal() = replace(" ", "").let { nonEmpty ->
    nonEmpty.whether { contains(".") }
        ?.substring(0, nonEmpty.indexOf("."))
        ?: run { nonEmpty }
}

fun Date.dateStringToFormattedString(): String =
    SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(this)

fun CurrencyResponse.toRates(): Rates {
    val rate = rates
    rate.base = base
    // todo need to change to CurrencyResponse.date when BE return date
    rate.date = Date().dateStringToFormattedString()
    return rate
}

@SuppressLint("DefaultLocale")
inline fun <reified T> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        kermit.e(e) { e.message.toString() }
        null
    }
}

fun Rates.toOfflineRates() = OfflineRates(
    base, date, aED, aFN, aLL, aMD, aNG, aOA, aRS, aUD, aWG, aZN, bAM, bBD, bDT, bGN, bHD,
    bIF, bMD, bND, bOB, bRL, bSD, bTC, bTN, bWP, bYN, bYR, bZD, cAD, cDF, cHF, cLF, cLP,
    cNY, cOP, cRC, cUC, cUP, cVE, cZK, dJF, dKK, dOP, dZD, eGP, eRN, eTB, eUR, fJD, fKP,
    gBP, gEL, gGP, gHS, gIP, gMD, gNF, gTQ, gYD, hKD, hNL, hRK, hTG, hUF, iDR, iLS, iMP,
    iNR, iQD, iRR, iSK, jEP, jMD, jOD, jPY, kES, kGS, kHR, kMF, kPW, kRW, kWD, kYD, kZT,
    lAK, lBP, lKR, lRD, lSL, lTL, lVL, lYD, mAD, mDL, mGA, mKD, mMK, mNT, mOP, mRO, mUR,
    mVR, mWK, mXN, mYR, mZN, nAD, nGN, nIO, nOK, nPR, nZD, oMR, pAB, pEN, pGK, pHP, pKR,
    pLN, pYG, qAR, rON, rSD, rUB, rWF, sAR, sBD, sCR, sDG, sEK, sGD, sHP, sLL, sOS, sRD,
    sTD, sVC, sYP, sZL, tHB, tJS, tMT, tND, tOP, tRY, tTD, tWD, tZS, uAH, uGX, uSD, uYU,
    uZS, vEF, vES, vND, vUV, wST, xAF, xAG, xAU, xCD, xDR, xOF, xPF, yER, zAR, zMK, zMW, zWL
)

fun OfflineRates.toRates() = Rates(
    base, date, aED, aFN, aLL, aMD, aNG, aOA, aRS, aUD, aWG, aZN, bAM, bBD, bDT, bGN, bHD,
    bIF, bMD, bND, bOB, bRL, bSD, bTC, bTN, bWP, bYN, bYR, bZD, cAD, cDF, cHF, cLF, cLP,
    cNY, cOP, cRC, cUC, cUP, cVE, cZK, dJF, dKK, dOP, dZD, eGP, eRN, eTB, eUR, fJD, fKP,
    gBP, gEL, gGP, gHS, gIP, gMD, gNF, gTQ, gYD, hKD, hNL, hRK, hTG, hUF, iDR, iLS, iMP,
    iNR, iQD, iRR, iSK, jEP, jMD, jOD, jPY, kES, kGS, kHR, kMF, kPW, kRW, kWD, kYD, kZT,
    lAK, lBP, lKR, lRD, lSL, lTL, lVL, lYD, mAD, mDL, mGA, mKD, mMK, mNT, mOP, mRO, mUR,
    mVR, mWK, mXN, mYR, mZN, nAD, nGN, nIO, nOK, nPR, nZD, oMR, pAB, pEN, pGK, pHP, pKR,
    pLN, pYG, qAR, rON, rSD, rUB, rWF, sAR, sBD, sCR, sDG, sEK, sGD, sHP, sLL, sOS, sRD,
    sTD, sVC, sYP, sZL, tHB, tJS, tMT, tND, tOP, tRY, tTD, tWD, tZS, uAH, uGX, uSD, uYU,
    uZS, vEF, vES, vND, vUV, wST, xAF, xAG, xAU, xCD, xDR, xOF, xPF, yER, zAR, zMK, zMW, zWL
)
